<template>
  <div class="bg-card-bg p-6 rounded-lg shadow-lg border border-card-border h-full flex flex-col items-center justify-center">
    <h3 class="text-2xl font-semibold mb-6 text-white text-center">Chance of Winning Title</h3>

    <div class="relative w-48 h-48 flex items-center justify-center">
      <div
        class="absolute inset-0 rounded-full"
        style="background: conic-gradient(
          var(--color-chart-green) 0% 35%,
          var(--color-chart-red) 35% 75%,
          #333 75% 100%
        );">
      </div>

      <div class="absolute w-36 h-36 bg-card-bg rounded-full"></div>

      <span class="relative text-5xl font-bold z-10 text-white">65%</span>
    </div>
  </div>
</template>

<style scoped>
/* Define the CSS variables for the gradient */
div[style] {
  --color-chart-green: #00B16A;
  --color-chart-red: #E10600;
}
</style>
