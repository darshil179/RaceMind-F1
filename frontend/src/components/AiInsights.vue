<template>
  <div class="bg-gradient-to-br from-gray-900/80 via-gray-900 to-black border border-gray-700 rounded-lg p-6 shadow-2xl relative overflow-hidden">
    <!-- Subtle overlay -->
    <div class="absolute inset-0 bg-gradient-to-br from-gray-800/10 to-transparent pointer-events-none"></div>

    <div class="relative z-10">
      <h2 class="text-3xl font-bold mb-6 bg-gradient-to-r from-white to-gray-300 bg-clip-text text-transparent">AI Race Insights</h2>

      <div class="flex gap-3">
        <input
          v-model="query"
          @keyup.enter="askAI"
          type="text"
          placeholder="Ask AI about the next race..."
          class="flex-1 bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-red-600 transition-colors"
        />
        <button
          @click="askAI"
          :disabled="loading || !query.trim()"
          class="bg-red-600 hover:bg-red-700 disabled:bg-gray-700 disabled:cursor-not-allowed text-white px-8 py-3 rounded-lg font-semibold transition-colors"
        >
          {{ loading ? 'Thinking...' : 'Ask' }}
        </button>
      </div>

      <div v-if="response" class="mt-6 p-4 bg-gray-800/50 rounded-lg border border-gray-700">
        <div class="flex items-start gap-3">
          <div class="w-8 h-8 rounded-full bg-red-600 flex items-center justify-center flex-shrink-0">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path>
            </svg>
          </div>
          <div class="flex-1">
            <p class="text-gray-300 leading-relaxed">{{ response }}</p>
          </div>
        </div>
      </div>

      <div v-if="suggestions.length" class="mt-4 flex flex-wrap gap-2">
        <button
          v-for="(suggestion, index) in suggestions"
          :key="index"
          @click="query = suggestion; askAI()"
          class="text-sm bg-gray-800 hover:bg-gray-700 border border-gray-700 px-3 py-2 rounded-lg transition-colors"
        >
          {{ suggestion }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const query = ref('')
const response = ref('')
const loading = ref(false)

const suggestions = ref([
  'Who will win Monaco?',
  'Best overtaking spots?',
  'Weather forecast?',
  'Qualifying predictions?'
])

const askAI = async () => {
  if (!query.value.trim() || loading.value) return

  loading.value = true
  response.value = ''

  // Simulate AI response (replace with actual API call)
  setTimeout(() => {
    response.value = `Based on current standings and Monaco's unique street circuit characteristics, Max Verstappen has the statistical advantage. However, Charles Leclerc's familiarity with the track and Ferrari's recent upgrades make this race highly competitive. Weather conditions and qualifying position will be crucial factors.`
    loading.value = false
  }, 1500)
}
</script>
