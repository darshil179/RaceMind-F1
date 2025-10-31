<template>
  <div class="bg-gradient-to-br from-gray-900 to-black border border-gray-800 rounded-lg overflow-hidden hover:border-red-600 transition-all duration-300 group">
    <div class="p-6">
      <h3 class="text-2xl font-bold mb-1">{{ driver.name }}</h3>
      <p class="text-gray-400 mb-4">{{ driver.team }}</p>

      <div class="relative h-80 mb-4 bg-gray-800 rounded-lg overflow-hidden">
        <img
          :src="driver.image"
          :alt="driver.name"
          class="w-full h-full object-contain object-center"
          @error="handleImageError"
        />
        <div
          v-if="driver.insight"
          class="absolute bottom-0 left-0 right-0 bg-black/90 backdrop-blur-sm p-4 text-sm transform translate-y-full group-hover:translate-y-0 transition-transform duration-300"
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
