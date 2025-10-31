<template>
  <div class="bg-gradient-to-br from-red-950/40 via-gray-900 to-black border border-red-900/30 rounded-lg p-6 shadow-2xl relative overflow-hidden">
    <!-- Subtle red glow overlay -->
    <div class="absolute inset-0 bg-gradient-to-br from-red-900/10 to-transparent pointer-events-none"></div>

    <div class="relative z-10">
      <h2 class="text-2xl font-bold mb-6">Current Standings</h2>

      <div class="flex gap-6">
        <!-- Table Section -->
        <div class="flex-1 overflow-x-auto">
          <table class="w-full">
            <thead>
            <tr class="text-left border-b border-red-900/20">
              <th class="pb-3 text-gray-500 font-normal text-xs uppercase tracking-wider">Pos</th>
              <th class="pb-3 text-gray-500 font-normal text-xs uppercase tracking-wider">Driver</th>
              <th class="pb-3 text-gray-500 font-normal text-xs uppercase tracking-wider">Team</th>
              <th class="pb-3 text-gray-500 font-normal text-xs uppercase tracking-wider text-right">Points</th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="driver in standings"
              :key="driver.position"
              class="border-b border-red-900/10 hover:bg-red-950/20 transition-colors"
            >
              <td class="py-4">
                <div
                  class="w-10 h-10 rounded flex items-center justify-center font-bold text-base"
                  :class="getPositionClass(driver.position)"
                >
                  {{ driver.position }}
                </div>
              </td>
              <td class="py-4">
                <div class="font-semibold text-base">{{ driver.name }}</div>
                <div class="text-xs text-gray-500 mt-0.5">{{ driver.teamShort }}</div>
              </td>
              <td class="py-4 text-gray-400 text-sm">{{ driver.team }}</td>
              <td class="py-4 text-right font-bold text-lg">{{ driver.points }}</td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- Side bar chart -->
        <div class="flex flex-col justify-end items-end gap-2 pb-4">
          <div
            v-for="driver in standings"
            :key="driver.position"
            class="relative flex flex-col items-center justify-end"
            :style="{ height: getBarHeight(driver.points) + 'px' }"
          >
            <div
              class="w-12 flex items-end justify-center font-bold text-lg text-white rounded-t"
              :class="getBarColor(driver.position)"
              :style="{ height: getBarHeight(driver.points) + 'px' }"
            >
              <span class="mb-2">{{ driver.position }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const standings = ref([
  {
    position: 1,
    name: 'Max Verstappen',
    teamShort: 'Red Bull',
    team: 'Red Bull Racing',
    points: 284
  },
  {
    position: 2,
    name: 'Charles Leclerc',
    teamShort: 'Ferrari',
    team: 'Ferrari F1',
    points: 207
  },
  {
    position: 3,
    name: 'Lewis Hamilton',
    teamShort: 'Hamilton',
    team: 'Mercedes BES',
    points: 168
  }
])

const getPositionClass = (position) => {
  switch(position) {
    case 1:
      return 'bg-gradient-to-br from-blue-600 to-blue-800 text-white shadow-lg shadow-blue-600/50'
    case 2:
      return 'bg-gradient-to-br from-red-700 to-red-900 text-white shadow-lg shadow-red-700/50'
    case 3:
      return 'bg-gradient-to-br from-gray-600 to-gray-800 text-white shadow-lg shadow-gray-600/50'
    default:
      return 'bg-gradient-to-br from-gray-700 to-gray-900 text-white'
  }
}

const getBarColor = (position) => {
  switch(position) {
    case 1:
      return 'bg-gradient-to-t from-blue-600 to-blue-800'
    case 2:
      return 'bg-gradient-to-t from-red-700 to-red-900'
    case 3:
      return 'bg-gradient-to-t from-gray-600 to-gray-800'
    default:
      return 'bg-gray-700'
  }
}

const getBarHeight = (points) => {
  // Calculate height based on points (max 284 = 100% height)
  const maxHeight = 120 // max height in pixels
  const maxPoints = 284
  return Math.max(40, (points / maxPoints) * maxHeight)
}
</script>
