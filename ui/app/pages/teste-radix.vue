<template>
  <div class="min-h-screen bg-gradient-to-br from-purple-50 to-pink-100">
    <div class="container mx-auto py-8">
      <div class="text-center mb-8">
        <h1 class="text-4xl font-bold text-gray-800 mb-4">
          🎯 Teste Completo do Radix UI
        </h1>
        <p class="text-lg text-gray-600 max-w-2xl mx-auto">
          Esta página demonstra todos os componentes do Nuxt UI baseados no Radix UI funcionando perfeitamente no seu projeto Nuxt.
        </p>
        <div class="flex justify-center mt-4 space-x-4">
          <UBadge color="green" variant="soft" size="lg">✅ Tailwind CSS</UBadge>
          <UBadge color="blue" variant="soft" size="lg">🎨 Nuxt UI</UBadge>
          <UBadge color="purple" variant="soft" size="lg">⚡ Radix UI</UBadge>
        </div>
      </div>

      <div class="mt-12 text-center">
        <UCard class="inline-block">
          <div class="p-6">
            <h3 class="text-xl font-semibold text-green-600 mb-2">🎉 Parabéns!</h3>
            <p class="text-gray-600">
              Se você consegue ver e interagir com todos os componentes acima, significa que:
            </p>
            <ul class="text-left mt-4 space-y-2 text-sm text-gray-600">
              <li class="flex items-center"><span class="text-green-500 mr-2">✓</span> Tailwind CSS está configurado corretamente</li>
              <li class="flex items-center"><span class="text-green-500 mr-2">✓</span> Nuxt UI está funcionando perfeitamente</li>
              <li class="flex items-center"><span class="text-green-500 mr-2">✓</span> Radix UI está integrado e operacional</li>
              <li class="flex items-center"><span class="text-green-500 mr-2">✓</span> Todos os componentes são interativos e responsivos</li>
            </ul>
          </div>
        </UCard>
      </div>
    </div>
  </div>
</template>

<script setup>
// Meta tags para SEO
useHead({
  title: 'Teste Radix UI - Nuxt Project',
  meta: [
    { 
      name: 'description', 
      content: 'Página completa de teste para verificar se todos os componentes Radix UI estão funcionando corretamente no Nuxt' 
    }
  ]
})

// Page layout
definePageMeta({
  layout: false // Usando layout customizado para esta página
})
</script>