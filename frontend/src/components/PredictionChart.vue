<template>
  <section class="px-6 mt-8">
    <h3 class="text-xl font-semibold mb-4">Prediction</h3>
    <div class="max-w-sm mx-auto bg-gray-900 p-6 rounded-xl">
      <Doughnut :data="chartData" :options="chartOptions" />
    </div>
  </section>
</template>

<script setup>
import { Doughnut } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
} from "chart.js";

ChartJS.register(Title, Tooltip, Legend, ArcElement);

const chartData = {
  labels: ["Max Verstappen", "Charles Leclerc", "Lewis Hamilton"],
  datasets: [
    {
      data: [42, 40, 18],
      backgroundColor: ["#ef4444", "#eab308", "#3b82f6"],
      hoverOffset: 10,
    },
  ],
};

const chartOptions = {
  plugins: {
    legend: { labels: { color: "#ddd" } },
  },
};
</script>
