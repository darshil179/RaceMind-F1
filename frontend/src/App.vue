<template>
  <div class="min-h-screen bg-app-bg text-gray-200 font-sans">
    <AppHeader />

    <main class="container mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-10">

      <HeroSection />

      <section class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-10">

        <div class="lg:col-span-2">
          <StandingsTable :standings="standingsData" />
        </div>

        <div class="lg:col-span-1">
          <WinChanceChart />
        </div>
      </section>

      <section class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
        <DriverCard
          v-for="driver in featuredDrivers"
          :key="driver.name"
          :driver="driver"
        />
      </section>

      <section class="bg-card-bg p-6 rounded-lg shadow-lg border border-card-border">
        <h3 class="text-2xl font-semibold mb-4 text-white">AI Race Insights</h3>
        <div class="flex flex-col sm:flex-row gap-4">
          <input
            type="text"
            placeholder="Ask AI about the next race..."
            class="flex-grow bg-gray-800 border border-gray-700 rounded-md py-3 px-4 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-btn-red"
          >
          <button class="bg-btn-red hover:bg-logo-red text-white font-bold py-3 px-6 rounded-md transition-colors">
            Ask
          </button>
        </div>
      </section>

    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import AppHeader from './components/AppHeader.vue';
import HeroSection from './components/HeroSection.vue';
import StandingsTable from './components/StandingsTable.vue';
import WinChanceChart from './components/WinChanceChart.vue';
import DriverCard from './components/DriverCard.vue';

// --- Data (This would come from your racemind_db) ---

const standingsData = ref([
  { pos: 1, name: 'Max Verstappen', teamShort: 'Red Bull', team: 'Red Bull Racing', points: 284, barColor: 'bg-bar-blue', textColor: 'text-bar-blue' },
  { pos: 2, name: 'Charles Leclerc', teamShort: 'Ferrari', team: 'Ferrari', points: 207, barColor: 'bg-bar-red', textColor: 'text-bar-red' },
  { pos: 3, name: 'Lewis Hamilton', teamShort: 'Hamilton', team: 'Mercedes', points: 168, barColor: 'bg-bar-teal', textColor: 'text-bar-teal' },
]);

const featuredDrivers = ref([
  {
    name: 'Max Verstappen',
    team: 'Red Bull Racing',
    imgSrc: '/images/verstappen.jpg',
    borderColor: 'border-bar-blue', // Or 'border-bar-red' if you prefer
    info: 'Verstappen excels on bolin street circuits, and traditional tracks.'
  },
  {
    name: 'Charles Leclerc',
    team: 'Ferrari',
    imgSrc: '/images/leclerc.jpg',
    borderColor: 'border-bar-red'
  },
  {
    name: 'Lewis Hamilton',
    team: 'Mercedes',
    imgSrc: '/images/hamilton.jpg',
    borderColor: 'border-bar-teal'
  },
]);
</script>

<style>
/* You can add your @import for the 'Inter' font here in a main.css file */
</style>
