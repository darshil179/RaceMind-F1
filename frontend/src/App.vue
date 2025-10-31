<template>
  <div id="app" class="min-h-screen bg-black">
    <NavBar />
    <router-view v-if="useRouter" />
    <Home v-else />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from './components/NavBar.vue'
import Home from './views/Home.vue'

const useRouter = ref(false) // Set to true if using Vue Router
</script>
