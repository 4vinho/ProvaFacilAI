<script setup lang="ts">
import { ref, reactive } from 'vue'

definePageMeta({
  layout: 'main'
})

const currentView = ref('home')
const isLoading = ref(false)
const notification = ref('')

const examForm = reactive({
  titulo: '',
  descricao: '',
  palavrasChave: [] as string[],
  dificuldade: 'MEDIO',
  numeroQuestoes: 5,
  tipoQuestao: 'mista',
  idiomaResposta: 'Português'
})

const materialForm = reactive({
  titulo: '',
  conteudo: ''
})

const currentKeyword = ref('')
const generatedExam = ref(null)
const materials = ref([])

const API_BASE = 'http://localhost:8080/api'

const addKeyword = () => {
  if (currentKeyword.value.trim() && !examForm.palavrasChave.includes(currentKeyword.value.trim())) {
    examForm.palavrasChave.push(currentKeyword.value.trim())
    currentKeyword.value = ''
  }
}


const removeKeyword = (index: number) => {
  examForm.palavrasChave.splice(index, 1)
}


const generateExam = async () => {
  if (!examForm.titulo || !examForm.palavrasChave.length) {
    notification.value = 'Título e palavras-chave são obrigatórios'
    setTimeout(() => notification.value = '', 3000)
    return
  }

  isLoading.value = true
  try {
    const response = await fetch(`${API_BASE}/provas/gerar`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(examForm)
    })

    if (response.ok) {
      generatedExam.value = await response.json()
      notification.value = 'Prova gerada com sucesso!'
      currentView.value = 'result'
    } else {
      const error = await response.text()
      notification.value = `Erro: ${error}`
    }
  } catch (error) {
    notification.value = 'Erro ao conectar com o servidor'
  } finally {
    isLoading.value = false
    setTimeout(() => notification.value = '', 3000)
  }
}

const uploadMaterial = async () => {
  if (!materialForm.titulo || !materialForm.conteudo) {
    notification.value = 'Título e conteúdo são obrigatórios'
    setTimeout(() => notification.value = '', 3000)
    return
  }

  isLoading.value = true
  try {
    const requestData = {
      titulo: materialForm.titulo,
      conteudo: materialForm.conteudo,
      descricao: materialForm.conteudo,
      palavrasChave: [] // Array vazio já que removemos o campo
    }
    
    const response = await fetch(`${API_BASE}/materiais/upload`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestData)
    })

    if (response.ok) {
      notification.value = 'Material enviado com sucesso!'
      Object.assign(materialForm, {
        titulo: '',
        conteudo: ''
      })
      loadMaterials()
    } else {
      const error = await response.text()
      notification.value = `Erro: ${error}`
    }
  } catch (error) {
    notification.value = 'Erro ao conectar com o servidor'
  } finally {
    isLoading.value = false
    setTimeout(() => notification.value = '', 3000)
  }
}

const loadMaterials = async () => {
  try {
    const response = await fetch(`${API_BASE}/materiais/meus-materiais`)
    if (response.ok) {
      materials.value = await response.json()
    }
  } catch (error) {
    console.error('Erro ao carregar materiais:', error)
  }
}

const resetExamForm = () => {
  Object.assign(examForm, {
    titulo: '',
    descricao: '',
    palavrasChave: [],
    dificuldade: 'MEDIO',
    numeroQuestoes: 5,
    tipoQuestao: 'mista',
    idiomaResposta: 'Português'
  })
  generatedExam.value = null
}

onMounted(() => {
  loadMaterials()
})
</script>

<template>
  <div class="min-h-screen relative overflow-hidden">
    <!-- Background gradient -->
    <div class="absolute inset-0 bg-gradient-to-br from-purple-900 via-blue-900 to-indigo-900"></div>
    
    <!-- Floating elements for depth -->
    <div class="absolute top-20 left-10 w-72 h-72 bg-purple-500 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-pulse"></div>
    <div class="absolute top-40 right-10 w-96 h-96 bg-blue-500 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-pulse delay-1000"></div>
    <div class="absolute bottom-20 left-1/3 w-80 h-80 bg-indigo-500 rounded-full mix-blend-multiply filter blur-xl opacity-20 animate-pulse delay-2000"></div>

    <!-- Notification -->
    <div v-if="notification" class="fixed top-4 right-4 z-50 glass-card p-4 text-white font-medium rounded-lg">
      {{ notification }}
    </div>

    <!-- Main content -->
    <div class="relative z-10 min-h-screen flex flex-col">
      <!-- Header -->
      <header class="glass-nav py-6">
        <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex items-center justify-between">
            <h1 class="text-3xl font-bold text-white">ProvaFacilAI</h1>
            <nav class="flex space-x-4">
              <button 
                @click="currentView = 'home'" 
                :class="['glass-button px-4 py-2 rounded-lg text-white font-medium', currentView === 'home' ? 'glass-bg-primary' : '']"
              >
                Início
              </button>
              <button 
                @click="currentView = 'create'" 
                :class="['glass-button px-4 py-2 rounded-lg text-white font-medium', currentView === 'create' ? 'glass-bg-primary' : '']"
              >
                Criar Prova
              </button>
              <button 
                @click="currentView = 'materials'" 
                :class="['glass-button px-4 py-2 rounded-lg text-white font-medium', currentView === 'materials' ? 'glass-bg-primary' : '']"
              >
                Materiais
              </button>
            </nav>
          </div>
        </div>
      </header>

      <!-- Main content area -->
      <main class="flex-1 max-w-6xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        
        <!-- Home View -->
        <div v-if="currentView === 'home'" class="text-center">
          <div class="glass-card p-12 rounded-2xl">
            <h2 class="text-5xl font-bold text-white mb-6 glass-float">
              Bem-vindo ao ProvaFacilAI
            </h2>
            <p class="text-xl text-glass-muted mb-12 max-w-3xl mx-auto">
              Sua plataforma inteligente para criação e gestão de provas usando IA. 
              Faça upload de materiais e gere provas personalizadas automaticamente.
            </p>
            
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
              <div class="glass-card glass-hover p-8 rounded-xl cursor-pointer" @click="currentView = 'create'">
                <div class="flex flex-col items-center">
                  <div class="glass-bg-primary p-4 rounded-full mb-4">
                    <Icon name="lucide:file-text" class="h-8 w-8 text-white" />
                  </div>
                  <h3 class="text-xl font-semibold text-white mb-2">Criar Provas</h3>
                  <p class="text-glass-muted text-center">Gere provas automaticamente com IA baseada em seus materiais</p>
                </div>
              </div>

              <div class="glass-card glass-hover p-8 rounded-xl cursor-pointer" @click="currentView = 'materials'">
                <div class="flex flex-col items-center">
                  <div class="glass-bg-primary p-4 rounded-full mb-4">
                    <Icon name="lucide:upload" class="h-8 w-8 text-white" />
                  </div>
                  <h3 class="text-xl font-semibold text-white mb-2">Upload Materiais</h3>
                  <p class="text-glass-muted text-center">Faça upload e gerencie seus materiais de estudo</p>
                </div>
              </div>

              <div class="glass-card glass-hover p-8 rounded-xl">
                <div class="flex flex-col items-center">
                  <div class="glass-bg-primary p-4 rounded-full mb-4">
                    <Icon name="lucide:bar-chart" class="h-8 w-8 text-white" />
                  </div>
                  <h3 class="text-xl font-semibold text-white mb-2">Estatísticas</h3>
                  <p class="text-glass-muted text-center">Acompanhe o desempenho e estatísticas</p>
                </div>
              </div>
            </div>

            <button 
              @click="currentView = 'create'" 
              class="glass-button px-8 py-4 rounded-xl text-white font-semibold text-lg glass-hover"
            >
              Começar Agora
            </button>
          </div>
        </div>

        <!-- Create Exam View -->
        <div v-if="currentView === 'create'" class="max-w-4xl mx-auto">
          <div class="glass-card p-8 rounded-2xl">
            <h2 class="text-3xl font-bold text-white mb-8 text-center">Criar Nova Prova</h2>
            
            <form @submit.prevent="generateExam" class="space-y-6">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-white font-medium mb-2">Título da Prova</label>
                  <input 
                    v-model="examForm.titulo"
                    type="text" 
                    class="glass-input w-full px-4 py-3 rounded-lg text-white placeholder-gray-300"
                    placeholder="Ex: Prova de Matemática"
                    required
                  />
                </div>

                <div>
                  <label class="block text-white font-medium mb-2">Número de Questões</label>
                  <select v-model="examForm.numeroQuestoes" class="glass-input w-full px-4 py-3 rounded-lg text-white">
                    <option value="3">3 questões</option>
                    <option value="5">5 questões</option>
                    <option value="10">10 questões</option>
                    <option value="15">15 questões</option>
                    <option value="20">20 questões</option>
                  </select>
                </div>
              </div>

              <div>
                <label class="block text-white font-medium mb-2">Descrição</label>
                <textarea 
                  v-model="examForm.descricao"
                  class="glass-input w-full px-4 py-3 rounded-lg text-white placeholder-gray-300 h-24"
                  placeholder="Descreva o contexto e objetivos da prova..."
                ></textarea>
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-white font-medium mb-2">Dificuldade</label>
                  <select v-model="examForm.dificuldade" class="glass-input w-full px-4 py-3 rounded-lg text-white">
                    <option value="FACIL">Fácil</option>
                    <option value="MEDIO">Médio</option>
                    <option value="DIFICIL">Difícil</option>
                  </select>
                </div>

                <div>
                  <label class="block text-white font-medium mb-2">Tipo de Questão</label>
                  <select v-model="examForm.tipoQuestao" class="glass-input w-full px-4 py-3 rounded-lg text-white">
                    <option value="mista">Mista</option>
                    <option value="multipla-escolha">Múltipla Escolha</option>
                    <option value="dissertativa">Dissertativa</option>
                  </select>
                </div>
              </div>

              <div>
                <label class="block text-white font-medium mb-2">Palavras-Chave</label>
                <div class="flex gap-2 mb-3">
                  <input 
                    v-model="currentKeyword"
                    @keyup.enter="addKeyword"
                    type="text" 
                    class="glass-input flex-1 px-4 py-3 rounded-lg text-white placeholder-gray-300"
                    placeholder="Digite uma palavra-chave e pressione Enter"
                  />
                  <button 
                    type="button"
                    @click="addKeyword"
                    class="glass-button px-6 py-3 rounded-lg text-white font-medium"
                  >
                    Adicionar
                  </button>
                </div>
                <div class="flex flex-wrap gap-2">
                  <span 
                    v-for="(keyword, index) in examForm.palavrasChave" 
                    :key="index"
                    class="glass-badge px-3 py-1 rounded-full text-white text-sm flex items-center gap-2"
                  >
                    {{ keyword }}
                    <button @click="removeKeyword(index)" class="text-red-300 hover:text-red-100">
                      <Icon name="lucide:x" class="h-4 w-4" />
                    </button>
                  </span>
                </div>
              </div>

              <div class="flex justify-center pt-6">
                <button 
                  type="submit"
                  :disabled="isLoading"
                  class="glass-button px-8 py-4 rounded-xl text-white font-semibold text-lg disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <Icon v-if="isLoading" name="lucide:loader-2" class="h-5 w-5 mr-2 animate-spin" />
                  {{ isLoading ? 'Gerando Prova...' : 'Gerar Prova' }}
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- Materials View -->
        <div v-if="currentView === 'materials'" class="max-w-4xl mx-auto">
          <div class="glass-card p-8 rounded-2xl mb-8">
            <h2 class="text-3xl font-bold text-white mb-8 text-center">Gerenciar Materiais</h2>
            
            <form @submit.prevent="uploadMaterial" class="space-y-6">
              <div>
                <label class="block text-white font-medium mb-2">Título do Material</label>
                <input 
                  v-model="materialForm.titulo"
                  type="text" 
                  class="glass-input w-full px-4 py-3 rounded-lg text-white placeholder-gray-300"
                  placeholder="Ex: Apostila de Cálculo I"
                  required
                />
              </div>

              <div>
                <label class="block text-white font-medium mb-2">Conteúdo do Material</label>
                <textarea 
                  v-model="materialForm.conteudo"
                  class="glass-input w-full px-4 py-3 rounded-lg text-white placeholder-gray-300 h-40"
                  placeholder="Cole aqui o conteúdo do seu material de estudo..."
                  required
                ></textarea>
              </div>


              <div class="flex justify-center">
                <button 
                  type="submit"
                  :disabled="isLoading"
                  class="glass-button px-8 py-4 rounded-xl text-white font-semibold text-lg disabled:opacity-50"
                >
                  <Icon v-if="isLoading" name="lucide:loader-2" class="h-5 w-5 mr-2 animate-spin" />
                  {{ isLoading ? 'Enviando...' : 'Enviar Material' }}
                </button>
              </div>
            </form>
          </div>

          <!-- Materials List -->
          <div v-if="materials.length > 0" class="glass-card p-8 rounded-2xl">
            <h3 class="text-2xl font-bold text-white mb-6">Seus Materiais</h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div 
                v-for="material in materials" 
                :key="material.id"
                class="glass-card glass-hover p-6 rounded-xl"
              >
                <h4 class="text-lg font-semibold text-white mb-2">{{ material.title }}</h4>
                <p class="text-glass-muted text-sm">{{ material.description }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Exam Result View -->
        <div v-if="currentView === 'result' && generatedExam" class="max-w-4xl mx-auto">
          <div class="glass-card p-8 rounded-2xl">
            <div class="flex justify-between items-start mb-8">
              <div>
                <h2 class="text-3xl font-bold text-white mb-2">{{ generatedExam.titulo }}</h2>
                <p class="text-glass-muted">{{ generatedExam.descricao }}</p>
                <div class="flex gap-4 mt-4">
                  <span class="glass-badge px-3 py-1 rounded-full text-white text-sm">
                    {{ generatedExam.dificuldade }}
                  </span>
                  <span class="glass-badge px-3 py-1 rounded-full text-white text-sm">
                    {{ generatedExam.questoes?.length || 0 }} questões
                  </span>
                  <span class="glass-badge px-3 py-1 rounded-full text-white text-sm">
                    {{ generatedExam.idioma }}
                  </span>
                </div>
              </div>
              <button 
                @click="resetExamForm(); currentView = 'create'"
                class="glass-button px-4 py-2 rounded-lg text-white font-medium"
              >
                Nova Prova
              </button>
            </div>

            <div class="space-y-6">
              <div 
                v-for="(questao, index) in generatedExam.questoes" 
                :key="index"
                class="glass-card p-6 rounded-xl"
              >
                <h3 class="text-lg font-semibold text-white mb-4">
                  Questão {{ index + 1 }}
                </h3>
                <div class="text-glass mb-4 whitespace-pre-wrap">{{ questao.pergunta }}</div>
                <div v-if="questao.resposta" class="glass-bg-subtle p-4 rounded-lg">
                  <h4 class="text-sm font-medium text-white mb-2">Resposta:</h4>
                  <div class="text-glass-muted text-sm whitespace-pre-wrap">{{ questao.resposta }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

      </main>
    </div>
  </div>
</template>