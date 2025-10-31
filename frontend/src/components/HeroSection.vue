<template>
  <div class="relative h-[500px] overflow-hidden">
    <!-- Background Image -->
    <div
      class="absolute inset-0 bg-cover bg-center"
      style="background-image: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.7)), url('/images/monaco.png')"
    ></div>

    <!-- Content -->
    <div class="relative h-full flex flex-col justify-center items-start max-w-7xl mx-auto px-6 pt-20">
      <h1 class="text-5xl md:text-7xl font-bold mb-6 text-white drop-shadow-lg">Monaco Grand Prix</h1>
      <div class="text-xl md:text-2xl text-white">
        Race starts in:
        <span class="font-mono font-bold text-red-600 ml-2 text-3xl">{{ countdown }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const countdown = ref('05:12:26')
let interval = null

// Calculate countdown to race start
const updateCountdown = () => {
  const now = new Date()
  // Set race date (example: 5 hours from now)
  const raceDate = new Date(now.getTime() + (5 * 60 * 60 * 1000) + (12 * 60 * 1000) + (26 * 1000))

  const diff = raceDate - now

  if (diff > 0) {
    const hours = Math.floor(diff / (1000 * 60 * 60))
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
    const seconds = Math.floor((diff % (1000 * 60)) / 1000)

    countdown.value = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
  } else {
    countdown.value = 'RACE STARTED!'
  }
}

onMounted(() => {
  updateCountdown()
  interval = setInterval(updateCountdown, 1000)
})

onUnmounted(() => {
  if (interval) clearInterval(interval)
})
</script>
