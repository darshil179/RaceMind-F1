<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <HeroSection />

    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-6 py-12">
      <!-- Standings and Chart Grid -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-12">
        <div>
          <StandingsTable />
        </div>
        <div>
          <WinningChanceChart />
        </div>
      </div>

      <!-- Driver Cards -->
      <div class="mb-12">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <DriverCard
            v-for="driver in drivers"
            :key="driver.id"
            :driver="driver"
          />
        </div>
      </div>

      <!-- AI Insights -->
      <AIInsights />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import HeroSection from '../components/HeroSection.vue'
import StandingsTable from '../components/StandingsTable.vue'
import WinningChanceChart from '../components/WinningChanceChart.vue'
import DriverCard from '../components/DriverCard.vue'
import AIInsights from '../components/AIInsights.vue'

const drivers = ref([
  {
    id: 1,
    name: 'Max Verstappen',
    team: 'Red Bull Racing',
    image: 'https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/M/MAXVER01_Max_Verstappen/maxver01.png.transform/2col/image.png',
    insight: 'Verstappen excels on street circuits and traditional tracks.'
  },
  {
    id: 2,
    name: 'Charles Leclerc',
    team: 'Ferrari',
    image: 'https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/C/CHALEC01_Charles_Leclerc/chalec01.png.transform/2col/image.png',
    insight: 'Monaco specialist with home advantage.'
  },
  {
    id: 3,
    name: 'Lewis Hamilton',
    team: 'Mercedes',
    image: 'https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/L/LEWHAM01_Lewis_Hamilton/lewham01.png.transform/2col/image.png',
    insight: 'Seven-time champion seeking redemption.'
  }
])
</script>
