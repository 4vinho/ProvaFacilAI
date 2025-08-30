<script setup lang="ts">

</script>

<template>
  <div class="min-h-screen">
    <!-- Header -->
    <header class="glass-nav sticky top-0 z-50 p-4">
      <div class="container mx-auto flex items-center justify-between">
        <h1 class="text-glass text-xl font-bold">ProvaFácil AI</h1>
        <nav class="flex space-x-4">
          <NuxtLink to="/" class="text-glass hover:text-white transition-colors">Home</NuxtLink>
          <NuxtLink to="/dashboard" class="text-glass hover:text-white transition-colors">Dashboard</NuxtLink>
        </nav>
      </div>
    </header>

    <div class="flex">
      <!-- Sidebar -->
      <aside class="glass-sidebar w-64 min-h-screen p-4">
        <nav class="space-y-2">
          <NuxtLink to="/dashboard" class="glass-button block p-3 rounded-lg text-glass">
            Dashboard
          </NuxtLink>
          <NuxtLink to="/analytics" class="glass-button block p-3 rounded-lg text-glass">
            Analytics
          </NuxtLink>
          <NuxtLink to="/settings" class="glass-button block p-3 rounded-lg text-glass">
            Settings
          </NuxtLink>
        </nav>
      </aside>

      <!-- Main Content -->
      <main class="flex-1 p-6">
        <slot />
      </main>
    </div>
  </div>
</template>

<style scoped>

</style>