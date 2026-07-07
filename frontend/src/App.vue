<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { HelloApi, type HelloResponse } from '@example/shared'

const api = new HelloApi('/api')
const response = ref<HelloResponse | null>(null)
const error = ref<string | null>(null)
const loading = ref(false)

async function loadHello(): Promise<void> {
  loading.value = true
  error.value = null

  try {
    response.value = await api.loadHello()
  } catch (caught) {
    error.value = caught instanceof Error ? caught.message : String(caught)
  } finally {
    loading.value = false
  }
}

onMounted(loadHello)
</script>

<template>
  <main class="page-shell">
    <section class="card">
      <p class="eyebrow">Oddjobs vertical slice</p>
      <h1>Hello World</h1>

      <p v-if="loading" class="state">Loading database-backed greeting…</p>
      <p v-else-if="error" class="state error">{{ error }}</p>
      <p v-else-if="response" class="message">{{ response.message }}</p>
      <p v-else class="state">No greeting loaded yet.</p>

      <button type="button" :disabled="loading" @click="loadHello">
        {{ loading ? 'Refreshing…' : 'Refresh' }}
      </button>
    </section>
  </main>
</template>
