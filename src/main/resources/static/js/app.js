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
      view_list: "Modo Lista",
      view_kanban: "Modo Kanban",
      table_title: "Título",
      table_status: "Status",
      table_schedule: "Agendada para",
      table_description: "Descrição",
      table_actions: "Ações",
      kanban_pending: "Pendentes",
      kanban_in_progress: "Em progresso",
      kanban_completed: "Concluídas",
      kanban_title_placeholder: "Título da tarefa",
      kanban_desc_placeholder: "Descrição (opcional)",
      kanban_add: "Adicionar",
      modal_new_task: "Nova Tarefa",
      modal_edit_task: "Editar Tarefa",
      label_title: "Título",
      label_description: "Descrição",
      label_schedule: "Agendar para",
      label_status: "Status",
      option_pending: "Pendente",
      option_in_progress: "Em progresso",
      option_completed: "Concluída",
      btn_cancel: "Cancelar",
      btn_save: "Salvar",
      btn_edit: "Editar",
      btn_done: "Concluir",
      btn_delete: "Excluir",
      empty_kanban: "Sem tarefas",
      no_schedule: "Sem agendamento",
      toast_load_error: "Erro ao carregar tarefas",
      toast_title_required: "Informe um título",
      toast_save_ok: "Tarefa salva com sucesso",
      toast_save_error: "Erro ao salvar tarefa",
      toast_create_error: "Erro ao criar tarefa",
      toast_status_ok: "Status atualizado",
      toast_status_error: "Erro ao atualizar",
      toast_delete_ok: "Tarefa excluída",
      toast_delete_error: "Erro ao excluir",
      no_data: "Sem dados",
      report_error: "Erro ao carregar Relatório",
      report_updated: "Relatório atualizado",
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
      view_list: "List View",
      view_kanban: "Kanban View",
      table_title: "Title",
      table_status: "Status",
      table_schedule: "Scheduled for",
      table_description: "Description",
      table_actions: "Actions",
      kanban_pending: "Pending",
      kanban_in_progress: "In progress",
      kanban_completed: "Completed",
      kanban_title_placeholder: "Task title",
      kanban_desc_placeholder: "Description (optional)",
      kanban_add: "Add",
      modal_new_task: "New Task",
      modal_edit_task: "Edit Task",
      label_title: "Title",
      label_description: "Description",
      label_schedule: "Schedule for",
      label_status: "Status",
      option_pending: "Pending",
      option_in_progress: "In progress",
      option_completed: "Completed",
      btn_cancel: "Cancel",
      btn_save: "Save",
      btn_edit: "Edit",
      btn_done: "Done",
      btn_delete: "Delete",
      empty_kanban: "No tasks",
      no_schedule: "No schedule",
      toast_load_error: "Failed to load tasks",
      toast_title_required: "Title is required",
      toast_save_ok: "Task saved",
      toast_save_error: "Failed to save task",
      toast_create_error: "Failed to create task",
      toast_status_ok: "Status updated",
      toast_status_error: "Failed to update",
      toast_delete_ok: "Task deleted",
      toast_delete_error: "Failed to delete",
      no_data: "No data",
      report_error: "Failed to load report",
      report_updated: "Report updated",
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
      view_list: "Vista de lista",
      view_kanban: "Vista Kanban",
      table_title: "Título",
      table_status: "Estado",
      table_schedule: "Programada para",
      table_description: "Descripción",
      table_actions: "Acciones",
      kanban_pending: "Pendientes",
      kanban_in_progress: "En progreso",
      kanban_completed: "Completadas",
      kanban_title_placeholder: "Título de la tarea",
      kanban_desc_placeholder: "Descripción (opcional)",
      kanban_add: "Agregar",
      modal_new_task: "Nueva Tarea",
      modal_edit_task: "Editar Tarea",
      label_title: "Título",
      label_description: "Descripción",
      label_schedule: "Programar para",
      label_status: "Estado",
      option_pending: "Pendiente",
      option_in_progress: "En progreso",
      option_completed: "Completada",
      btn_cancel: "Cancelar",
      btn_save: "Guardar",
      btn_edit: "Editar",
      btn_done: "Concluir",
      btn_delete: "Eliminar",
      empty_kanban: "Sin tareas",
      no_schedule: "Sin programación",
      toast_load_error: "Error al cargar tareas",
      toast_title_required: "Ingrese un título",
      toast_save_ok: "Tarea guardada",
      toast_save_error: "Error al guardar tarea",
      toast_create_error: "Error al crear tarea",
      toast_status_ok: "Estado actualizado",
      toast_status_error: "Error al actualizar",
      toast_delete_ok: "Tarea eliminada",
      toast_delete_error: "Error al eliminar",
      no_data: "Sin datos",
      report_error: "Error al cargar reporte",
      report_updated: "Reporte actualizado",
    },
  };

  function getDict(lang) {
    return translations[lang] || translations["pt-br"];
  }

  function translateStatus(status, lang) {
    const dict = getDict(lang);
    if (!status) return "";
    switch (status) {
      case "PENDING":
        return dict.status_pending || status;
      case "IN_PROGRESS":
        return dict.status_in_progress || status;
      case "COMPLETED":
        return dict.status_completed || status;
      default:
        return status;
    }
  }

  function translateKey(key, lang) {
    const dict = getDict(lang);
    return dict[key] || key;
  }

  function applyI18n(lang) {
    const dict = getDict(lang);
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

  return { initPage, fetchSettings, translateStatus, translateKey };
})();
