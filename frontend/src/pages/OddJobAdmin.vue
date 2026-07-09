<script setup lang="ts">
import { HttpOddjobRepository, CreateOddjobRequest } from '@oddjobs/shared'
import { reactive, ref, toRaw } from 'vue'
var test = ref(0)

const form = reactive(
  new CreateOddjobRequest(
    '',
    ''
  )
)

function submitForm() {
    new HttpOddjobRepository().createOddJob(toRaw(form))
}


</script>

<template>
  <h1>Oddjob Admin</h1>
  <p>This is where you define Odd jobs</p>


    <form @submit.prevent="submitForm">
        <label>Name</label>
        <input v-model="form.name" /></br>
        <label>Prompt</label>
        <textarea v-model="form.prompt" />

         <button type="submit">
              Create
            </button>

    </form>
</template>