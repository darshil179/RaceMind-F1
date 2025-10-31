<template>
  <nav class="fixed top-0 left-0 right-0 z-50 bg-black/80 backdrop-blur-sm border-b border-gray-800">
    <div class="max-w-7xl mx-auto px-6 py-4">
      <div class="flex items-center justify-between">
        <div class="text-2xl font-bold">
          Race<span class="text-red-600">Mind</span>
        </div>

        <div class="hidden md:flex items-center space-x-8">
          <a href="#" class="text-white hover:text-red-600 transition-colors">Home</a>
          <a href="#" class="text-white hover:text-red-600 transition-colors">Drivers</a>
          <a href="#" class="text-white hover:text-red-600 transition-colors">Predictions</a>
        </div>

        <button @click="toggleMenu" class="md:hidden text-white">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
          </svg>
        </button>
      </div>

      <!-- Mobile Menu -->
      <div v-if="menuOpen" class="md:hidden mt-4 pb-4 space-y-4">
        <a href="#" class="block text-white hover:text-red-600 transition-colors">Home</a>
        <a href="#" class="block text-white hover:text-red-600 transition-colors">Drivers</a>
        <a href="#" class="block text-white hover:text-red-600 transition-colors">Predictions</a>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'

const menuOpen = ref(false)

const toggleMenu = () => {
  menuOpen.value = !menuOpen.value
}
</script>
