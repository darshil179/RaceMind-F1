<template>
  <header
    class="fixed top-0 left-0 w-full z-50 bg-black/60 backdrop-blur-lg border-b border-red-600 shadow-lg"
  >
    <nav
      class="max-w-7xl mx-auto flex justify-between items-center px-6 py-3 text-white"
    >
      <!-- Logo -->
      <div class="flex items-center space-x-2">
<!--        <img-->
<!--          src="/"-->
<!--          alt="RaceMind Logo"-->
<!--          class="w-8 h-8"-->
<!--        />-->
        <h1 class="text-2xl font-extrabold tracking-wide text-red-500">
          RaceMind
        </h1>
      </div>

      <!-- Navigation Links -->
      <ul class="hidden md:flex space-x-8 text-sm font-medium">
        <li><a href="#" class="hover:text-red-500 transition">Home</a></li>
        <li><a href="#" class="hover:text-red-500 transition">Drivers</a></li>
        <li><a href="#" class="hover:text-red-500 transition">Standings</a></li>
        <li><a href="#" class="hover:text-red-500 transition">Predictions</a></li>
        <li><a href="#" class="hover:text-red-500 transition">About</a></li>
      </ul>

      <!-- Theme toggle and AI icon -->
      <div class="flex items-center space-x-4">
        <!-- Dark/Light Mode Toggle -->
        <button
          @click="toggleDarkMode"
          class="p-2 rounded-full bg-gray-800 hover:bg-red-600 transition"
        >
          <i
            :class="darkMode ? 'fas fa-sun text-yellow-400' : 'fas fa-moon text-gray-300'"
          ></i>
        </button>

        <!-- AI Chat Icon -->
        <button
          class="p-2 rounded-full bg-gray-800 hover:bg-red-600 transition"
          title="Ask AI"
        >
          <i class="fas fa-robot text-gray-200"></i>
        </button>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref, watchEffect } from 'vue'

const darkMode = ref(false)

const toggleDarkMode = () => {
  darkMode.value = !darkMode.value
}

// Apply dark/light mode to <html> element
watchEffect(() => {
  const html = document.documentElement
  if (darkMode.value) {
    html.classList.add('dark')
  } else {
    html.classList.remove('dark')
  }
})
</script>

<style scoped>
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css');
</style>
