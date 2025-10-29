<template>
  <div class="bg-card-bg p-6 rounded-lg shadow-lg border border-card-border h-full">
    <h3 class="text-2xl font-semibold mb-6 text-white">Current Standings</h3>

    <div class="grid grid-cols-12 gap-4 px-3 pb-3 text-sm text-gray-400 font-medium border-b border-card-border">
      <div class="col-span-1">Pos</div>
      <div class="col-span-3">Driver</div>
      <div class="col-span-3">Team</div>
      <div class="col-span-2 text-right">Points</div>
      <div class="col-span-3"></div> </div>

    <div class="space-y-2 mt-4">
      <div v-for="driver in standings" :key="driver.pos"
           class="grid grid-cols-12 gap-4 items-center px-3 py-3 rounded-md hover:bg-gray-800 transition-colors">

        <div class="col-span-1 text-lg font-bold text-white">{{ driver.pos }}</div>

        <div class="col-span-3">
          <span class="block font-semibold text-lg text-white">{{ driver.name }}</span>
          <span class="text-sm text-gray-400">{{ driver.teamShort }}</span>
        </div>

        <div class="col-span-3 text-gray-300">{{ driver.team }}</div>

        <div class="col-span-2 text-right text-lg font-bold text-white">{{ driver.points }}</div>

        <div class="col-span-3 flex items-center space-x-2">
          <div class="h-8 rounded-sm" :class="driver.barColor" :style="{ width: getBarWidth(driver.points) }"></div>
          <span class="font-bold text-lg" :class="driver.textColor">{{ driver.pos }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  standings: Array
});

// Find max points to make the bar proportional
const maxPoints = computed(() => {
  return Math.max(...props.standings.map(d => d.points));
});

// Calculate bar width as a percentage
const getBarWidth = (points) => {
  return (points / maxPoints.value) * 70 + '%'; // 70% max width
};
</script>
