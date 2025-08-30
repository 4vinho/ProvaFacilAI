<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-8">
    <div class="max-w-4xl mx-auto">
      <!-- Header -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold text-gray-800 mb-4">
          Teste do Tailwind CSS + Radix UI
        </h1>
        <p class="text-lg text-gray-600">
          Esta página testa se o Tailwind CSS e Radix UI estão funcionando corretamente
        </p>
      </div>

      <!-- Cards Grid -->
      <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6 mb-12">
        <!-- Card 1 - Colors & Typography -->
        <div class="bg-white rounded-xl shadow-lg p-6 border border-gray-200">
          <h3 class="text-xl font-semibold text-gray-800 mb-4">Cores & Tipografia</h3>
          <div class="space-y-3">
            <div class="p-3 bg-red-100 text-red-800 rounded-lg">Vermelho</div>
            <div class="p-3 bg-green-100 text-green-800 rounded-lg">Verde</div>
            <div class="p-3 bg-blue-100 text-blue-800 rounded-lg">Azul</div>
          </div>
        </div>

        <!-- Card 2 - Spacing & Layout -->
        <div class="bg-white rounded-xl shadow-lg p-6 border border-gray-200">
          <h3 class="text-xl font-semibold text-gray-800 mb-4">Espaçamento</h3>
          <div class="space-y-2">
            <div class="w-full h-4 bg-purple-200 rounded"></div>
            <div class="w-3/4 h-4 bg-purple-300 rounded ml-4"></div>
            <div class="w-1/2 h-4 bg-purple-400 rounded ml-8"></div>
            <div class="w-1/4 h-4 bg-purple-500 rounded ml-12"></div>
          </div>
        </div>

        <!-- Card 3 - Flexbox -->
        <div class="bg-white rounded-xl shadow-lg p-6 border border-gray-200">
          <h3 class="text-xl font-semibold text-gray-800 mb-4">Flexbox</h3>
          <div class="flex justify-between items-center space-x-2">
            <div class="w-12 h-12 bg-orange-400 rounded-full"></div>
            <div class="w-12 h-12 bg-orange-500 rounded-full"></div>
            <div class="w-12 h-12 bg-orange-600 rounded-full"></div>
          </div>
        </div>
      </div>

      <!-- Radix UI Components -->
      <div class="bg-white rounded-xl shadow-lg p-8 border border-gray-200 mb-12">
        <h3 class="text-2xl font-semibold text-gray-800 mb-6">Componentes Radix UI</h3>
        
        <div class="grid md:grid-cols-2 gap-8">
          <!-- Buttons -->
          <div>
            <h4 class="text-lg font-medium text-gray-700 mb-4">Botões</h4>
            <div class="space-y-3">
              <UButton variant="solid" color="primary" size="lg" class="w-full">
                Botão Primary
              </UButton>
              <UButton variant="outline" color="gray" size="md" class="w-full">
                Botão Outline
              </UButton>
              <UButton variant="soft" color="green" size="sm" class="w-full">
                Botão Soft
              </UButton>
            </div>
          </div>

          <!-- Inputs -->
          <div>
            <h4 class="text-lg font-medium text-gray-700 mb-4">Inputs</h4>
            <div class="space-y-4">
              <UInput placeholder="Digite seu nome..." />
              <UTextarea placeholder="Digite uma mensagem..." :rows="3" />
              <USelect>
                <option value="">Selecione uma opção</option>
                <option value="1">Opção 1</option>
                <option value="2">Opção 2</option>
                <option value="3">Opção 3</option>
              </USelect>
            </div>
          </div>
        </div>
      </div>

      <!-- Interactive Section -->
      <div class="bg-white rounded-xl shadow-lg p-8 border border-gray-200">
        <h3 class="text-2xl font-semibold text-gray-800 mb-6">Seção Interativa</h3>
        
        <div class="grid md:grid-cols-2 gap-8">
          <!-- Alerts -->
          <div>
            <h4 class="text-lg font-medium text-gray-700 mb-4">Alertas</h4>
            <div class="space-y-4">
              <UAlert 
                icon="i-heroicons-information-circle" 
                color="blue" 
                variant="soft"
                title="Informação"
                description="Este é um alerta informativo"
              />
              <UAlert 
                icon="i-heroicons-check-circle" 
                color="green" 
                variant="soft"
                title="Sucesso"
                description="Operação realizada com sucesso!"
              />
              <UAlert 
                icon="i-heroicons-exclamation-triangle" 
                color="yellow" 
                variant="soft"
                title="Atenção"
                description="Verifique as informações"
              />
            </div>
          </div>

          <!-- Progress & Loading -->
          <div>
            <h4 class="text-lg font-medium text-gray-700 mb-4">Progresso</h4>
            <div class="space-y-4">
              <div>
                <p class="text-sm text-gray-600 mb-2">Progresso: 75%</p>
                <UProgress :value="75" />
              </div>
              <div>
                <p class="text-sm text-gray-600 mb-2">Carregando...</p>
                <div class="flex items-center space-x-3">
                  <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-500"></div>
                  <span class="text-sm text-gray-600">Processando...</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="mt-8 pt-6 border-t border-gray-200">
          <div class="flex flex-wrap gap-4">
            <UButton @click="showSuccess" color="green">
              Teste Sucesso
            </UButton>
            <UButton @click="showError" color="red">
              Teste Erro
            </UButton>
            <UButton @click="showInfo" color="blue">
              Teste Info
            </UButton>
          </div>
        </div>
      </div>

      <!-- Back to Home -->
      <div class="text-center mt-12">
        <NuxtLink to="/">
          <UButton variant="outline" size="lg">
            ← Voltar para Home
          </UButton>
        </NuxtLink>
      </div>

      <!-- Footer -->
      <div class="text-center mt-12 p-6 bg-white rounded-xl shadow-lg">
        <p class="text-gray-600">
          Se você consegue ver todos os estilos aplicados corretamente, o Tailwind CSS está funcionando! 🎉
        </p>
        <p class="text-sm text-gray-500 mt-2">
          Se os componentes Radix estão renderizando, o Nuxt UI também está funcionando!
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
// Função para mostrar notificações de teste
const toast = useToast()

const showSuccess = () => {
  toast.add({
    title: 'Sucesso!',
    description: 'O Radix UI está funcionando perfeitamente!',
    color: 'green'
  })
}

const showError = () => {
  toast.add({
    title: 'Erro de Teste',
    description: 'Este é apenas um teste de notificação de erro.',
    color: 'red'
  })
}

const showInfo = () => {
  toast.add({
    title: 'Informação',
    description: 'Componentes carregados com sucesso!',
    color: 'blue'
  })
}

// Meta tags
useHead({
  title: 'Teste Tailwind CSS + Radix UI',
  meta: [
    { name: 'description', content: 'Página de teste para verificar se Tailwind CSS e Radix UI estão funcionando' }
  ]
})
</script>