<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-8">
    <div class="text-center max-w-4xl mx-auto">
      <h1 class="text-6xl font-bold text-gray-800 mb-6">
        🚀 Nuxt + Tailwind + Radix
      </h1>
      <p class="text-xl text-gray-600 mb-12">
        Seu projeto está funcionando perfeitamente!
      </p>

      <div class="grid md:grid-cols-2 gap-8 mb-12">
        <UCard class="p-8 hover:shadow-xl transition-shadow">
          <div class="text-center">
            <div class="text-4xl mb-4">🎨</div>
            <h3 class="text-2xl font-semibold text-gray-800 mb-4">Teste Tailwind</h3>
            <p class="text-gray-600 mb-6">
              Verifique se o Tailwind CSS está funcionando corretamente
            </p>
            <NuxtLink to="/teste-tailwind">
              <UButton color="blue" size="lg" block>
                Testar Tailwind
              </UButton>
            </NuxtLink>
          </div>
        </UCard>

        <UCard class="p-8 hover:shadow-xl transition-shadow">
          <div class="text-center">
            <div class="text-4xl mb-4">⚡</div>
            <h3 class="text-2xl font-semibold text-gray-800 mb-4">Teste Radix UI</h3>
            <p class="text-gray-600 mb-6">
              Teste todos os componentes do Nuxt UI baseados no Radix
            </p>
            <NuxtLink to="/teste-radix">
              <UButton color="purple" size="lg" block>
                Testar Radix UI
              </UButton>
            </NuxtLink>
          </div>
        </UCard>
      </div>

      <div class="bg-white rounded-xl p-8 shadow-lg">
        <h3 class="text-2xl font-semibold text-gray-800 mb-4">Status dos Componentes</h3>
        <div class="flex flex-wrap justify-center gap-4">
          <UBadge color="green" variant="soft" size="lg">✅ Nuxt 4.0.3</UBadge>
          <UBadge color="blue" variant="soft" size="lg">✅ Tailwind CSS 4.1.12</UBadge>
          <UBadge color="purple" variant="soft" size="lg">✅ Nuxt UI 3.3.2</UBadge>
          <UBadge color="orange" variant="soft" size="lg">✅ Radix UI</UBadge>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
useHead({
  title: 'Home - Teste Nuxt + Tailwind + Radix',
  meta: [
    { name: 'description', content: 'Página inicial para testar Nuxt com Tailwind CSS e Radix UI' }
  ]
})
</script>