#!/usr/bin/env bash
set -Eeuo pipefail

LOG_FILE="${ODDJOBS_DEV_LOG:-/var/log/oddjobs/dev.log}"
DB_HOST="${ODDJOBS_DB_HOST:-postgres}"
DB_PORT="${ODDJOBS_DB_PORT:-5432}"
DB_USER="${ODDJOBS_DB_USER:-helloworld}"
DB_NAME="${ODDJOBS_DB_NAME:-helloworld}"
SPRING_PORT="${SERVER_PORT:-9991}"
VITE_PORT="${VITE_PORT:-9992}"

GRADLE_PROJECT_CACHE_ROOT="${GRADLE_PROJECT_CACHE_ROOT:-/tmp/oddjobs-gradle-project-cache}"

mkdir -p "$(dirname "$LOG_FILE")" /home/oddjobs/.gradle /home/oddjobs/.cache "$GRADLE_PROJECT_CACHE_ROOT"
: > "$LOG_FILE"

log() {
  printf '[%s] %s\n' "$(date -Iseconds)" "$*" | tee -a "$LOG_FILE" >&2
}

run_logged() {
  local __pid_var="$1"
  local name="$2"
  shift 2
  log "starting ${name}: $*"
  {
    printf '\n===== %s started at %s =====\n' "$name" "$(date -Iseconds)"
    "$@"
    status=$?
    printf '===== %s exited with status %s at %s =====\n' "$name" "$status" "$(date -Iseconds)"
    exit "$status"
  } >> "$LOG_FILE" 2>&1 &
  printf -v "$__pid_var" '%s' "$!"
}

wait_for_postgres() {
  log "waiting for PostgreSQL at ${DB_HOST}:${DB_PORT}/${DB_NAME}"
  until PGPASSWORD="${ODDJOBS_DB_PASSWORD:-helloworld}" pg_isready -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" >> "$LOG_FILE" 2>&1; do
    sleep 2
  done
  log "PostgreSQL is ready"
}

export GRADLE_USER_HOME="${GRADLE_USER_HOME:-/home/oddjobs/.gradle}"
export COREPACK_HOME="${COREPACK_HOME:-/home/oddjobs/.cache/corepack}"
export ODDJOBS_DB_URL="${ODDJOBS_DB_URL:-jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}}"
export ODDJOBS_DB_USER="$DB_USER"
export ODDJOBS_DB_PASSWORD="${ODDJOBS_DB_PASSWORD:-helloworld}"
export SERVER_PORT="$SPRING_PORT"
export VITE_BACKEND_PROXY_TARGET="${VITE_BACKEND_PROXY_TARGET:-http://localhost:${SPRING_PORT}}"

python3 scripts/serve-log.py >> "$LOG_FILE" 2>&1 &
LOG_SERVER_PID=$!
log "serving combined dev log on port 9994 from ${LOG_FILE}"

wait_for_postgres

log "installing pnpm workspace dependencies"
if ! pnpm install --frozen-lockfile -force >> "$LOG_FILE" 2>&1; then
  log "pnpm install failed; keeping log server alive for inspection"
  wait "$LOG_SERVER_PID"
fi

log "running one-time Gradle bootstrap with global Gradle $(gradle --version | sed -n '3p')"
if ! gradle --project-cache-dir "$GRADLE_PROJECT_CACHE_ROOT/bootstrap" -Poddjobs.skipDbUp=true bootstrap >> "$LOG_FILE" 2>&1; then
  log "Gradle bootstrap failed; keeping log server alive for inspection"
  wait "$LOG_SERVER_PID"
fi

run_logged COMPILE_PID "gradle-continuous-classess" \
  gradle -Poddjobs.skipDbUp=true :shared:stageNpmPackage :backend:classes --continuous

log "Waiting for background build to start ..."
sleep 15s

run_logged BOOT_PID "gradle-bootstrap-bootRun" \
  gradle --project-cache-dir "$GRADLE_PROJECT_CACHE_ROOT/boot" -Poddjobs.skipDbUp=true bootstrap :backend:bootRun


run_logged FRONTEND_PID "pnpm-frontend-dev" \
  pnpm --dir frontend dev --host 0.0.0.0 --port "$VITE_PORT" --strictPort

shutdown() {
  log "shutting down dev processes"
  kill "$COMPILE_PID" "$BOOT_PID" "$FRONTEND_PID" "$LOG_SERVER_PID" 2>/dev/null || true
  wait 2>/dev/null || true
}
trap shutdown INT TERM EXIT

wait -n "$COMPILE_PID" "$BOOT_PID" "$FRONTEND_PID"
EXITED=$?
log "a foreground dev process exited; see log above"
exit "$EXITED"
