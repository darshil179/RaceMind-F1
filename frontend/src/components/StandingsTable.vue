<template>
  <div class="bg-gradient-to-br from-gray-900 to-black border border-gray-800 rounded-lg p-8">
    <h2 class="text-3xl font-bold mb-8">Current Standings</h2>

    <div class="overflow-x-auto">
      <table class="w-full">
        <thead>
        <tr class="text-left border-b border-gray-700">
          <th class="pb-4 text-gray-400 font-medium text-sm uppercase tracking-wider">Pos</th>
          <th class="pb-4 text-gray-400 font-medium text-sm uppercase tracking-wider">Driver</th>
          <th class="pb-4 text-gray-400 font-medium text-sm uppercase tracking-wider">Team</th>
          <th class="pb-4 text-gray-400 font-medium text-sm uppercase tracking-wider text-right">Points</th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="driver in standings"
          :key="driver.position"
          class="border-b border-gray-800 hover:bg-gray-900/50 transition-colors"
        >
          <td class="py-6">
            <div
              class="w-12 h-12 rounded-lg flex items-center justify-center font-bold text-lg"
              :class="getPositionClass(driver.position)"
            >
              {{ driver.position }}
            </div>
          </td>
          <td class="py-6">
            <div class="font-bold text-lg">{{ driver.name }}</div>
            <div class="text-sm text-gray-400 mt-1">{{ driver.teamShort }}</div>
          </td>
          <td class="py-6 text-gray-300">{{ driver.team }}</td>
          <td class="py-6 text-right font-bold text-2xl">{{ driver.points }}</td>
        </tr>
        </tbody>
      </table>
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
      return 'bg-blue-600 text-white'
    case 2:
      return 'bg-red-800 text-white'
    case 3:
      return 'bg-gray-700 text-white'
    default:
      return 'bg-gray-800 text-white'
  }
}
</script>
