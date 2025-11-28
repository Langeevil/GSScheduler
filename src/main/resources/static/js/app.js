const AppSettings = (() => {
  let cache = null;

  async function fetchSettings() {
    if (cache) return cache;
    try {
      const res = await fetch("/api/settings");
      if (!res.ok) throw new Error("settings fetch error");
      cache = await res.json();
    } catch (e) {
      cache = {
        theme: "light",
        language: "pt-br",
        emailNotifications: true,
        intervalHours: 4,
        retainHistory: true,
      };
    }
    return cache;
  }

  function applyTheme(theme) {
    const darkId = "app-dark-style";
    if (theme === "dark") {
      if (!document.getElementById(darkId)) {
        const style = document.createElement("style");
        style.id = darkId;
        style.textContent = `
          body.theme-dark {
            background: radial-gradient(circle at 20% 20%, #1f1f2e, #0d0d12 60%);
            color: #e0e0e0;
          }
          body.theme-dark .header,
          body.theme-dark .content,
          body.theme-dark .card,
          body.theme-dark .tasks-section,
          body.theme-dark .login-container {
            background: #181820;
            color: #e0e0e0;
            box-shadow: 0 2px 12px rgba(0,0,0,0.5);
          }
          body.theme-dark .btn-primary { background: linear-gradient(135deg, #5a67d8 0%, #805ad5 100%); }
          body.theme-dark .btn-secondary { background: #333; color: #e0e0e0; }
          body.theme-dark table { color: #e0e0e0; }
          body.theme-dark th { background: #2a2a34; color: #e0e0e0; border-color: #3a3a46; }
          body.theme-dark td { border-color: #2a2a34; }
          body.theme-dark .empty-state { color: #bcbcbc; }
        `;
        document.head.appendChild(style);
      }
      document.body.classList.add("theme-dark");
    } else {
      document.body.classList.remove("theme-dark");
    }
  }

  const translations = {
    "pt-br": {
      tasks_title: "Minhas Tarefas",
      dashboard: "Dashboard",
      tasks: "Tarefas",
      settings: "Configurações",
      reports: "Relatórios",
      back_dashboard: "Voltar ao Dashboard",
      new_task: "+ Nova Tarefa",
      empty_tasks: "Nenhuma tarefa cadastrada. Clique em \"+ Nova Tarefa\" para criar.",
      list_title: "Lista de Tarefas",
      status_pending: "Pendente",
      status_in_progress: "Em progresso",
      status_completed: "Concluída",
      recent_tasks: "Tarefas Recentes",
      no_tasks_dashboard: "Nenhuma tarefa para exibir.",
      go_tasks: "Ir para Tarefas",
      filters: "Filtros",
      generate_report: "Gerar Relatório",
    },
    en: {
      tasks_title: "My Tasks",
      dashboard: "Dashboard",
      tasks: "Tasks",
      settings: "Settings",
      reports: "Reports",
      back_dashboard: "Back to Dashboard",
      new_task: "+ New Task",
      empty_tasks: "No tasks yet. Click \"+ New Task\" to create.",
      list_title: "Task List",
      status_pending: "Pending",
      status_in_progress: "In progress",
      status_completed: "Completed",
      recent_tasks: "Recent Tasks",
      no_tasks_dashboard: "No tasks to show.",
      go_tasks: "Go to Tasks",
      filters: "Filters",
      generate_report: "Generate Report",
    },
    es: {
      tasks_title: "Mis Tareas",
      dashboard: "Panel",
      tasks: "Tareas",
      settings: "Configuraciones",
      reports: "Informes",
      back_dashboard: "Volver al Panel",
      new_task: "+ Nueva Tarea",
      empty_tasks: "Sin tareas. Haz clic en \"+ Nueva Tarea\" para crear.",
      list_title: "Lista de Tareas",
      status_pending: "Pendiente",
      status_in_progress: "En progreso",
      status_completed: "Completada",
      recent_tasks: "Tareas Recientes",
      no_tasks_dashboard: "Sin tareas para mostrar.",
      go_tasks: "Ir a Tareas",
      filters: "Filtros",
      generate_report: "Generar Informe",
    },
  };

  function applyI18n(lang) {
    const dict = translations[lang] || translations["pt-br"];
    document.querySelectorAll("[data-i18n]").forEach((el) => {
      const key = el.getAttribute("data-i18n");
      if (dict[key]) el.textContent = dict[key];
    });
  }

  async function initPage(afterSettings) {
    const settings = await fetchSettings();
    applyTheme(settings.theme);
    applyI18n(settings.language);
    if (afterSettings) afterSettings(settings);
  }

  return { initPage, fetchSettings };
})();
