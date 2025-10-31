<template>
  <div class="bg-gradient-to-br from-gray-900 via-gray-800 to-black border border-gray-700 rounded-lg overflow-hidden hover:border-red-600 hover:shadow-2xl hover:shadow-red-900/20 transition-all duration-300 group">
    <div class="p-6">
      <h3 class="text-2xl font-bold mb-1 bg-gradient-to-r from-white to-gray-300 bg-clip-text text-transparent">{{ driver.name }}</h3>
      <p class="text-gray-400 mb-4">{{ driver.team }}</p>

      <div class="relative h-80 mb-4 bg-gradient-to-b from-gray-700 to-gray-900 rounded-lg overflow-hidden">
        <img
          :src="driver.image"
          :alt="driver.name"
          class="w-full h-full object-contain object-center"
          @error="handleImageError"
        />
        <div
          v-if="driver.insight"
          class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black via-black/95 to-transparent backdrop-blur-sm p-4 text-sm transform translate-y-full group-hover:translate-y-0 transition-transform duration-300"
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

const handleImageError = (e) => {
  e.target.src = 'https://via.placeholder.com/400x600/1a1a1a/ffffff?text=Driver+Image'
}
</script>
