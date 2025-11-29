const AppSettings = (() => {
  let cache = null;

  async function fetchSettings() {
    if (cache) return cache;
    try {
      const savedTheme = localStorage.getItem("gs_theme");
      cache = {
        theme: savedTheme || "light",
        language: "pt-br",
        intervalHours: 4,
        retentionDays: 30,
        twoFactorEnabled: false,
      };
    } catch (e) {
      cache = null;
    }
    try {
      const res = await fetch("/api/settings");
      if (!res.ok) throw new Error("settings fetch error");
      cache = await res.json();
    } catch (e) {
      cache = {
        theme: "light",
        language: "pt-br",
        intervalHours: 4,
        retentionDays: 30,
        twoFactorEnabled: false,
      };
    }
    return cache;
  }

  function applyTheme(theme) {
    if (theme) {
      try { localStorage.setItem("gs_theme", theme); } catch (e) {}
    }
    const darkId = "app-dark-style";
    if (theme === "dark") {
      if (!document.getElementById(darkId)) {
        const style = document.createElement("style");
        style.id = darkId;
        style.textContent = `
          body.theme-dark {
            background: radial-gradient(circle at 20% 20%, #1f2340, #0b0d16 60%);
            color: #e8ecf6;
          }
          body.theme-dark .header,
          body.theme-dark .content,
          body.theme-dark .card,
          body.theme-dark .tasks-section,
          body.theme-dark .login-container {
            background: #1d2333;
            color: #e8ecf6;
            border: 1px solid #2a3144;
            box-shadow: 0 10px 30px rgba(0,0,0,0.5);
          }
          body.theme-dark h1,
          body.theme-dark h2,
          body.theme-dark h3,
          body.theme-dark label,
          body.theme-dark .setting-label {
            color: #e8ecf6;
          }
          body.theme-dark p,
          body.theme-dark .card-text,
          body.theme-dark .user-info,
          body.theme-dark .setting-description,
          body.theme-dark .breadcrumb {
            color: #b5bed5;
          }
          body.theme-dark a { color: #43c6ff; }
          body.theme-dark a:hover { color: #66d4ff; }
          body.theme-dark .card h2,
          body.theme-dark .tasks-section h2,
          body.theme-dark .content h2 {
            border-bottom-color: #6b7bff;
          }
          body.theme-dark .btn-primary,
          body.theme-dark .logout-btn,
          body.theme-dark button[type="submit"] {
            background: linear-gradient(135deg, #6b7bff 0%, #4f5ae0 100%);
            color: #e8ecf6;
            box-shadow: 0 8px 20px rgba(79,90,224,0.35);
          }
          body.theme-dark .btn-primary:hover,
          body.theme-dark .logout-btn:hover,
          body.theme-dark button[type="submit"]:hover {
            transform: translateY(-1px);
            box-shadow: 0 12px 24px rgba(79,90,224,0.4);
          }
          body.theme-dark .btn-secondary {
            background: #43c6ff;
            color: #06111d;
            box-shadow: 0 8px 18px rgba(67,198,255,0.25);
          }
          body.theme-dark .btn-secondary:hover { filter: brightness(1.08); transform: translateY(-1px); }
          body.theme-dark table { color: #e8ecf6; }
          body.theme-dark th {
            background: #1f2738;
            color: #e8ecf6;
            border-color: #2a3144;
          }
          body.theme-dark td { border-color: #2a3144; }
          body.theme-dark tr:hover { background: rgba(107,123,255,0.08); }
          body.theme-dark input[type="text"],
          body.theme-dark input[type="email"],
          body.theme-dark input[type="password"],
          body.theme-dark input[type="datetime-local"],
          body.theme-dark textarea,
          body.theme-dark select {
            background: #0f1422;
            color: #e8ecf6;
            border: 1px solid #2a3144;
          }
          body.theme-dark input:focus,
          body.theme-dark select:focus,
          body.theme-dark textarea:focus {
            outline: none;
            border-color: #6b7bff;
            box-shadow: 0 0 0 3px rgba(107,123,255,0.35);
          }
          body.theme-dark .toggle-switch .slider { background-color: #2a3144; }
          body.theme-dark .toggle-switch .slider:before { background-color: #e8ecf6; }
          body.theme-dark input:checked + .slider { background-color: #6b7bff; }
          body.theme-dark .card-icon {
            background: rgba(107,123,255,0.12);
            color: #9eb1ff;
          }
          body.theme-dark .card-icon svg { stroke: currentColor; }
          body.theme-dark .title-icon { color: #9eb1ff; }
          body.theme-dark .task-item {
            background: rgba(107,123,255,0.12);
            border-left-color: #6b7bff;
          }
          body.theme-dark .task-item h3 { color: #e8ecf6; }
          body.theme-dark .task-item p { color: #b5bed5; }
          body.theme-dark .task-status { background: rgba(107,123,255,0.25); color: #e8ecf6; }
          body.theme-dark .status-PENDING { background: rgba(245,196,81,0.18); color: #f5c451; }
          body.theme-dark .status-IN_PROGRESS { background: rgba(75,130,245,0.18); color: #6b7bff; }
          body.theme-dark .status-COMPLETED { background: rgba(83,209,139,0.2); color: #53d18b; }
          body.theme-dark .empty-state { color: #8a92a8; }
          body.theme-dark .role-badge { background: rgba(67,198,255,0.2); color: #e8ecf6; }
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
