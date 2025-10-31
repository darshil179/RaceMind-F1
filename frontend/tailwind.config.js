/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'race-red': '#dc2626',
        'race-blue': '#1e40af',
        'race-green': '#059669',
      }
    },
  },
  plugins: [],
}
