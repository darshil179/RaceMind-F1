<template>
  <div class="bg-card-bg rounded-lg shadow-lg overflow-hidden border border-card-border">
    <div classs="p-5 border-t-4" :class="driver.borderColor">
      <div class="p-5">
        <h4 class="text-2xl font-bold text-white">{{ driver.name }}</h4>
        <p class="text-gray-400">{{ driver.team }}</p>
      </div>
    </div>

    <div class="relative">
      <img :src="driver.imgSrc" :alt="driver.name" class="w-full h-auto">

      <div v-if="driver.info"
           class="absolute bottom-4 left-4 right-4 bg-black/70 p-3 rounded-md backdrop-blur-sm border border-gray-700">
        <p class="text-sm text-gray-200">{{ driver.info }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  driver: Object
});
</script>
