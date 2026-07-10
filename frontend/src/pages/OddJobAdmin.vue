<script setup lang="ts">
import { HttpOddjobRepository, SaveOddjobRequest } from '@oddjobs/shared'
import { reactive, ref, toRaw, onMounted, computed } from 'vue'
var test = ref(0)


const oddjobs = ref<Oddjob[]>([])
const loading = ref(false)

const form = reactive(
  new SaveOddjobRequest(
    null,
    '',
    ''
  )
)

const buttonLabel = computed(() =>
    "Save"
)
async function loadOddjobs() {
    loading.value = true
    try {
        oddjobs.value = (await new HttpOddjobRepository().listOddJobs()).items
    } catch(e) {
        console.log("error", e)
    } finally {
        loading.value = false
    }
}

onMounted(async () => {
    await loadOddjobs()
})




async function submitForm() {
    await new HttpOddjobRepository().createOddJob(toRaw(form))
    await loadOddjobs()
}


</script>

<template>
<div class="ui container">
  <h1>Oddjob Admin</h1>

    <h2>Create Oddjob</h2>
    <form class="ui form" @submit.prevent="submitForm">
    <div class="field">
        <label>Name</label>
        <input v-model="form.name" placeholder="Name of the Oddjob"/>
    </div>
    <div class="field">
        <label>Prompt</label>
        <textarea v-model="form.prompt" placeholder="Prompt for this Oddjob"/>
    </div>

     <button class="ui button" type="submit">
          {{buttonLabel}}
    </button>

    </form>

    <h2>Oddjob List</h2>
        <p v-if="loading">Loading oddjobs..</p>

        <table v-else class="ui table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Prompt</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="oddjob in oddjobs" :key="oddjob.id">
                    <td>{{ oddjob.name }}</td>
                    <td>{{ oddjob.prompt }}</td>
                </tr>
            </tbody>
        </table>

</div>
</template>