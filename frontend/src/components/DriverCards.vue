<template>
  <div class="bg-gradient-to-br from-gray-900 to-black border border-gray-800 rounded-lg overflow-hidden hover:border-red-600 transition-all duration-300 transform hover:scale-105">
    <div class="p-6">
      <h3 class="text-2xl font-bold mb-1">{{ driver.name }}</h3>
      <p class="text-gray-400 mb-4">{{ driver.team }}</p>

      <div class="relative h-64 mb-4">
        <img
          :src="driver.image"
          :alt="driver.name"
          class="w-full h-full object-cover rounded"
        />
        <div
          v-if="driver.insight"
          class="absolute bottom-0 left-0 right-0 bg-black/80 backdrop-blur-sm p-3 text-sm"
        >
          {{ driver.insight }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  driver: {
    type: Object,
    required: true
  }
})
</script>
