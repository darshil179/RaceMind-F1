// tailwind.config.js

/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // These are the specific colors from your image
        'app-bg': '#0D0D0D',      // The true black background
        'card-bg': '#1A1A1A',     // The dark gray for all cards
        'card-border': '#262626', // Subtle border color
        'logo-red': '#E10600',    // Bright F1-style red for logo/accents
        'btn-red': '#D90429',     // The "Ask" button red
        'bar-blue': '#007BFF',    // Verstappen's blue bar
        'bar-red': '#DC0000',     // Leclerc's red bar
        'bar-teal': '#00D2BE',    // Hamilton's teal color
        'chart-green': '#00B16A',
        'chart-red': '#E10600',
      },
      fontFamily: {
        // The font in your image looks like a clean sans-serif
        sans: ['Inter', 'system-ui', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
