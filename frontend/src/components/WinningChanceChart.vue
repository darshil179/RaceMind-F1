<template>
  <div class="bg-gradient-to-br from-blue-950/40 via-gray-900 to-black border border-blue-900/30 rounded-lg p-8 shadow-2xl relative overflow-hidden">
    <!-- Subtle blue glow overlay -->
    <div class="absolute inset-0 bg-gradient-to-br from-blue-900/10 to-transparent pointer-events-none"></div>

    <div class="relative z-10">
      <h2 class="text-3xl font-bold mb-8 bg-gradient-to-r from-white to-gray-300 bg-clip-text text-transparent">Chance of Winning Title</h2>

      <div class="flex justify-center items-center relative mb-8" style="height: 300px;">
        <canvas ref="chartCanvas"></canvas>
        <div class="absolute inset-0 flex items-center justify-center pointer-events-none">
          <div class="text-6xl font-bold">65%</div>
        </div>
      </div>

      <div class="space-y-3">
        <div class="flex items-center justify-between p-3 bg-gradient-to-r from-blue-900/30 to-gray-800/50 rounded-lg border border-blue-800/30">
          <div class="flex items-center">
            <div class="w-4 h-4 rounded-full bg-blue-600 mr-3 shadow-lg shadow-blue-600/50"></div>
            <span class="font-medium">Max Verstappen</span>
          </div>
          <span class="font-bold text-xl">65%</span>
        </div>
        <div class="flex items-center justify-between p-3 bg-gradient-to-r from-red-900/30 to-gray-800/50 rounded-lg border border-red-800/30">
          <div class="flex items-center">
            <div class="w-4 h-4 rounded-full bg-red-600 mr-3 shadow-lg shadow-red-600/50"></div>
            <span class="font-medium">Charles Leclerc</span>
          </div>
          <span class="font-bold text-xl">25%</span>
        </div>
        <div class="flex items-center justify-between p-3 bg-gradient-to-r from-green-900/30 to-gray-800/50 rounded-lg border border-green-800/30">
          <div class="flex items-center">
            <div class="w-4 h-4 rounded-full bg-green-600 mr-3 shadow-lg shadow-green-600/50"></div>
            <span class="font-medium">Lewis Hamilton</span>
          </div>
          <span class="font-bold text-xl">10%</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Chart, DoughnutController, ArcElement, Tooltip, Legend } from 'chart.js'

Chart.register(DoughnutController, ArcElement, Tooltip, Legend)

const chartCanvas = ref(null)

onMounted(() => {
  const ctx = chartCanvas.value.getContext('2d')

  new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Max Verstappen', 'Charles Leclerc', 'Lewis Hamilton'],
      datasets: [{
        data: [65, 25, 10],
        backgroundColor: [
          '#1e40af', // Blue
          '#dc2626', // Red
          '#059669'  // Green
        ],
        borderWidth: 0,
        cutout: '75%'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          enabled: true,
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          padding: 12,
          titleColor: '#fff',
          bodyColor: '#fff',
          callbacks: {
            label: function(context) {
              return context.label + ': ' + context.parsed + '%'
            }
          }
        }
      }
    }
  })
})
</script>
