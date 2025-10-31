<template>
  <div class="bg-gradient-to-br from-gray-900 to-black border border-gray-800 rounded-lg p-8">
    <h2 class="text-3xl font-bold mb-8">Chance of Winning Title</h2>

    <div class="flex justify-center items-center relative mb-8" style="height: 300px;">
      <canvas ref="chartCanvas"></canvas>
      <div class="absolute inset-0 flex items-center justify-center pointer-events-none">
        <div class="text-6xl font-bold">65%</div>
      </div>
    </div>

    <div class="space-y-3">
      <div class="flex items-center justify-between p-3 bg-gray-800/50 rounded-lg">
        <div class="flex items-center">
          <div class="w-4 h-4 rounded-full bg-blue-600 mr-3"></div>
          <span class="font-medium">Max Verstappen</span>
        </div>
        <span class="font-bold text-xl">65%</span>
      </div>
      <div class="flex items-center justify-between p-3 bg-gray-800/50 rounded-lg">
        <div class="flex items-center">
          <div class="w-4 h-4 rounded-full bg-red-600 mr-3"></div>
          <span class="font-medium">Charles Leclerc</span>
        </div>
        <span class="font-bold text-xl">25%</span>
      </div>
      <div class="flex items-center justify-between p-3 bg-gray-800/50 rounded-lg">
        <div class="flex items-center">
          <div class="w-4 h-4 rounded-full bg-green-600 mr-3"></div>
          <span class="font-medium">Lewis Hamilton</span>
        </div>
        <span class="font-bold text-xl">10%</span>
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
