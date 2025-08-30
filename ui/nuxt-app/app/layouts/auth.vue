<script setup lang="ts">

</script>

<template>
  <div class="min-h-screen flex flex-col">
    <!-- Main Content Area -->
    <main class="flex-1 flex items-center justify-center p-6">
      <div class="glass-card w-full max-w-md p-8">
        <slot />
      </div>
    </main>

    <!-- Auth Footer -->
    <footer class="glass-nav p-6 text-center">
      <div class="container mx-auto">
        <p class="text-glass-muted text-sm">
          © 2025 ProvaFácil AI. Todos os direitos reservados.
        </p>
        <div class="flex justify-center space-x-4 mt-2">
          <NuxtLink to="/privacy" class="text-glass-muted hover:text-glass text-sm transition-colors">
            Privacidade
          </NuxtLink>
          <NuxtLink to="/terms" class="text-glass-muted hover:text-glass text-sm transition-colors">
            Termos
          </NuxtLink>
          <NuxtLink to="/support" class="text-glass-muted hover:text-glass text-sm transition-colors">
            Suporte
          </NuxtLink>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>

</style>