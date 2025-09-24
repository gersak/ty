(ns ty.site.docs.modal
  (:require [ty.site.docs.common
             :refer [code-block
                     attribute-table
                     event-table
                     doc-section
                     example-section
                     placeholder-view]]
            [ty.site.state :as state]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-modal"]
    [:p.text-lg.ty-text-
     "A pure wrapper component for modal dialogs that provides backdrop, focus management, and keyboard interaction without imposing any styling on your content."]]

   ;; Key Characteristics
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text.mb-4 "Key Characteristics"]
    [:ul.space-y-2.ty-text-
     [:li "• " [:span.ty-text "Pure wrapper"] " - No styling imposed on content"]
     [:li "• " [:span.ty-text "Native dialog"] " - Built on browser's " [:code "dialog"] " element"]
     [:li "• " [:span.ty-text "Dual API"] " - Declarative (" [:code "open"] ") and imperative (" [:code "modal.show()"] ")"]
     [:li "• " [:span.ty-text "Protected mode"] " - Confirmation required for closing"]
     [:li "• " [:span.ty-text "Scroll locking"] " - Automatic body scroll management"]
     [:li "• " [:span.ty-text "Flexible backdrop"] " - Customizable or disabled"]
     [:li "• " [:span.ty-text "Focus trapping"] " - Keyboard navigation contained"]
     [:li "• " [:span.ty-text "Auto-hiding close button"] " - Shows on hover outside content area"]]]

   ;; API Reference
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text.mb-6 "API Reference"]

    ;; Attributes
    [:div.mb-6
     [:h3.text-lg.font-medium.ty-text.mb-3 "Attributes"]
     (attribute-table
      [{:name "open"
        :type "boolean"
        :default "false"
        :description "Controls modal visibility. When true, modal is shown."}
       {:name "backdrop"
        :type "boolean"
        :default "true"
        :description "Whether to show a backdrop behind the modal."}
       {:name "close-on-outside-click"
        :type "boolean"
        :default "true"
        :description "Whether clicking the backdrop closes the modal."}
       {:name "close-on-escape"
        :type "boolean"
        :default "true"
        :description "Whether pressing ESC key closes the modal."}
       {:name "protected"
        :type "boolean"
        :default "false"
        :description "When true, requires confirmation before closing (shows browser confirm dialog)."}])]

    ;; Methods
    [:div.mb-6
     [:h3.text-lg.font-medium.ty-text.mb-3 "Methods"]
     [:div.overflow-x-auto
      [:table.w-full
       [:thead
        [:tr.border-b.ty-border-
         [:th.text-left.py-2.px-3.ty-text.font-medium "Method"]
         [:th.text-left.py-2.px-3.ty-text.font-medium "Description"]]]
       [:tbody
        [:tr.border-b.ty-border--
         [:td.py-2.px-3 [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "show()"]]
         [:td.py-2.px-3.ty-text- "Opens the modal programmatically"]]
        [:tr.border-b.ty-border--
         [:td.py-2.px-3 [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "hide()"]]
         [:td.py-2.px-3.ty-text- "Closes the modal programmatically"]]]]]]

    ;; Events
    [:div.mb-6
     [:h3.text-lg.font-medium.ty-text.mb-3 "Events"]
     (event-table
      [{:name "ty-modal-open"
        :payload "{}"
        :when-fired "Fired when the modal opens"}
       {:name "ty-modal-close"
        :payload "{reason: 'programmatic'|'native', returnValue?: string}"
        :when-fired "Fired when the modal closes, includes reason and optional return value"}])]

    ;; Slots
    [:div
     [:h3.text-lg.font-medium.ty-text.mb-3 "Slots"]
     [:div.overflow-x-auto
      [:table.w-full
       [:thead
        [:tr.border-b.ty-border-
         [:th.text-left.py-2.px-3.ty-text.font-medium "Slot"]
         [:th.text-left.py-2.px-3.ty-text.font-medium "Description"]]]
       [:tbody
        [:tr.border-b.ty-border--
         [:td.py-2.px-3 [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "(default)"]]
         [:td.py-2.px-3.ty-text- "Modal content - any HTML, styled however you want"]]]]]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text.mb-4 "Basic Usage"]
    [:p.ty-text-.mb-4
     "The modal is a pure wrapper - all styling (size, background, borders, padding) is applied by your content."]

    (code-block
     "<!-- Button to open modal -->
<ty-button onclick=\"document.getElementById('my-modal').show()\">
  Open Modal
</ty-button>

<!-- Modal with styled content -->
<ty-modal id=\"my-modal\">
  <div class=\"p-6 max-w-md ty-elevated rounded-lg\">
    <h3 class=\"text-lg font-semibold mb-4\">Modal Title</h3>
    <p class=\"ty-text- mb-4\">This is your modal content.</p>
    <ty-button onclick=\"this.closest('ty-modal').hide()\">
      Close
    </ty-button>
  </div>
</ty-modal>")

    [:div.mt-4
     [:ty-button {:on {:click #(swap! state/state assoc :docs-modal-basic true)}}
      "Try Basic Example"]

     [:ty-modal {:id "docs-modal-basic"
                 :open (get @state/state :docs-modal-basic false)
                 :on {:ty-modal-close #(swap! state/state assoc :docs-modal-basic false)}}
      [:div.p-6.max-w-md.ty-elevated.rounded-lg
       [:h3.text-lg.font-semibold.mb-4 "Modal Title"]
       [:p.ty-text-.mb-4 "This is your modal content."]
       [:ty-button {:on {:click #(swap! state/state assoc :docs-modal-basic false)}}
        "Close"]]]]]

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Examples"]

   ;; Declarative Control
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "Declarative Control"]
    [:p.ty-text-.mb-4 "Control modal visibility using the " [:code "open"] " attribute."]

    (code-block
     "<!-- Using a checkbox to control modal -->
<input type=\"checkbox\" id=\"modal-toggle\" 
       onchange=\"document.getElementById('declarative-modal').open = this.checked\">
<label for=\"modal-toggle\">Toggle Modal</label>

<ty-modal id=\"declarative-modal\" open>
  <div class=\"p-6 ty-elevated rounded-lg\">
    <p>Modal controlled by checkbox</p>
  </div>
</ty-modal>")

    [:div.mt-4.flex.items-center.gap-3
     [:input {:type "checkbox"
              :id "modal-declarative-toggle"
              :on {:change #(swap! state/state assoc :docs-modal-declarative (.. % -target -checked))}}]
     [:label {:for "modal-declarative-toggle"} "Toggle Modal"]

     [:ty-modal {:open (get @state/state :docs-modal-declarative false)
                 :on {:ty-modal-close #(swap! state/state assoc :docs-modal-declarative false)}}
      [:div.p-6.ty-elevated.rounded-lg
       [:p.ty-text "Modal controlled by checkbox"]
       [:ty-button.mt-3 {:on {:click #(swap! state/state assoc :docs-modal-declarative false)}}
        "Close"]]]]]

   ;; Protected Mode
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "Protected Mode"]
    [:p.ty-text-.mb-4
     "Use " [:code "protected=\"true\""] " to require confirmation before closing. Useful for forms with unsaved changes."]

    (code-block
     "<ty-modal id=\"protected-modal\" protected=\"true\">
  <div class=\"p-6 max-w-md ty-elevated rounded-lg\">
    <h3 class=\"text-lg font-semibold mb-4\">Unsaved Changes</h3>
    <form>
      <input type=\"text\" placeholder=\"Make some changes...\">
      <p class=\"ty-text- mt-2\">
        Try closing this modal - you'll be asked to confirm.
      </p>
    </form>
  </div>
</ty-modal>")

    [:div.mt-4
     [:ty-button {:on {:click #(swap! state/state assoc :docs-modal-protected true)}}
      "Open Protected Modal"]

     [:ty-modal {:open (get @state/state :docs-modal-protected false)
                 :protected true
                 :on {:ty-modal-close #(swap! state/state assoc :docs-modal-protected false)}}
      [:div.p-6.max-w-md.ty-elevated.rounded-lg
       [:h3.text-lg.font-semibold.mb-4 "Unsaved Changes"]
       [:form
        [:ty-input.w-full {:type "text"
                           :placeholder "Make some changes..."}]
        [:p.ty-text-.mt-3.text-sm
         "Try closing this modal - you'll be asked to confirm."]]]]]]

   ;; No Backdrop
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "No Backdrop"]
    [:p.ty-text-.mb-4 "Disable the backdrop for a more integrated feel."]

    (code-block
     "<ty-modal backdrop=\"false\" close-on-outside-click=\"false\">
  <div class=\"p-6 ty-floating rounded-lg shadow-xl\">
    <p>Modal without backdrop - page content remains visible</p>
  </div>
</ty-modal>")

    [:div.mt-4
     [:ty-button {:on {:click #(swap! state/state assoc :docs-modal-no-backdrop true)}}
      "Open Modal (No Backdrop)"]

     [:ty-modal {:open (get @state/state :docs-modal-no-backdrop false)
                 :backdrop false
                 :close-on-outside-click false
                 :on {:ty-modal-close #(swap! state/state assoc :docs-modal-no-backdrop false)}}
      [:div.p-6.ty-floating.rounded-lg.shadow-xl
       [:p.ty-text.mb-3 "Modal without backdrop - page content remains visible"]
       [:ty-button {:on {:click #(swap! state/state assoc :docs-modal-no-backdrop false)}}
        "Close"]]]]]

   ;; Modal with Form
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "Modal with Form"]
    [:p.ty-text-.mb-4 "Common pattern for form dialogs with actions."]

    (code-block
     "<ty-modal id=\"form-modal\">
  <div class=\"p-6 max-w-lg ty-elevated rounded-lg\">
    <h3 class=\"text-xl font-semibold mb-4\">Create New Item</h3>
    
    <form onsubmit=\"handleSubmit(event)\">
      <div class=\"space-y-4\">
        <div>
          <label class=\"block text-sm font-medium mb-1\">Name</label>
          <ty-input type=\"text\" required></ty-input>
        </div>
        
        <div>
          <label class=\"block text-sm font-medium mb-1\">Category</label>
          <ty-dropdown placeholder=\"Choose category...\">
            <ty-option value=\"work\">Work</ty-option>
            <ty-option value=\"personal\">Personal</ty-option>
          </ty-dropdown>
        </div>
        
        <div>
          <label class=\"block text-sm font-medium mb-1\">Description</label>
          <ty-textarea rows=\"3\"></ty-textarea>
        </div>
      </div>
      
      <div class=\"flex justify-end gap-2 mt-6\">
        <ty-button type=\"button\" flavor=\"neutral\" 
                   onclick=\"this.closest('ty-modal').hide()\">
          Cancel
        </ty-button>
        <ty-button type=\"submit\" flavor=\"primary\">
          Create
        </ty-button>
      </div>
    </form>
  </div>
</ty-modal>")

    [:div.mt-4
     [:ty-button {:on {:click #(swap! state/state assoc :docs-modal-form true)}}
      "Open Form Modal"]

     [:ty-modal {:open (get @state/state :docs-modal-form false)
                 :on {:ty-modal-close #(swap! state/state assoc :docs-modal-form false)}}
      [:div.p-6.max-w-lg.ty-elevated.rounded-lg
       [:h3.text-xl.font-semibold.mb-4 "Create New Item"]

       [:form {:on {:submit (fn [e]
                              (.preventDefault e)
                              (js/alert "Form submitted!")
                              (swap! state/state assoc :docs-modal-form false))}}
        [:div.space-y-4
         [:div
          [:label.block.text-sm.font-medium.mb-1 "Name"]
          [:ty-input.w-full {:type "text"
                             :required true}]]

         [:div
          [:label.block.text-sm.font-medium.mb-1 "Category"]
          [:ty-dropdown.w-full {:placeholder "Choose category..."}
           [:ty-option {:value "work"} "Work"]
           [:ty-option {:value "personal"} "Personal"]]]

         [:div
          [:label.block.text-sm.font-medium.mb-1 "Description"]
          [:ty-textarea.w-full {:rows "3"}]]]

        [:div.flex.justify-end.gap-2.mt-6
         [:ty-button {:type "button"
                      :flavor "neutral"
                      :on {:click #(swap! state/state assoc :docs-modal-form false)}}
          "Cancel"]
         [:ty-button {:type "submit"
                      :flavor "primary"}
          "Create"]]]]]]]

   ;; Programmatic Control
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "Programmatic Control"]
    [:p.ty-text-.mb-4 "Control modals programmatically using JavaScript methods."]

    (code-block

     "// Get modal element
const modal = document.getElementById('my-modal');

// Open modal
modal.show();

// Close modal
modal.hide();

// Listen for events
modal.addEventListener('ty-modal-open', (e) => {
  console.log('Modal opened');
});

modal.addEventListener('ty-modal-close', (e) => {
  console.log('Modal closed:', e.detail.reason);
  // e.detail.reason can be 'programmatic' or 'native'
});

// Control attributes
modal.setAttribute('protected', 'true');
modal.backdrop = false;"
     "javascript")

    [:div.mt-4.space-y-3
     [:div.flex.gap-3
      [:ty-button {:on {:click #(.show (.getElementById js/document "docs-modal-programmatic"))}}
       "Open with .show()"]
      [:ty-button {:flavor "danger"
                   :on {:click #(.hide (.getElementById js/document "docs-modal-programmatic"))}}
       "Close with .hide()"]]

     [:ty-modal {:id "docs-modal-programmatic"
                 :on {:ty-modal-open #(js/console.log "Modal opened via event")
                      :ty-modal-close #(js/console.log "Modal closed:" (.-detail %))}}
      [:div.p-6.ty-elevated.rounded-lg.max-w-sm
       [:p.ty-text "This modal is controlled programmatically."]
       [:p.ty-text-.text-sm.mt-2 "Check console for event logs."]]]]]

   ;; Event Handling
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "Event Handling"]
    [:p.ty-text-.mb-4 "React to modal lifecycle events for analytics, cleanup, or state management."]

    (code-block

     "const modal = document.getElementById('analytics-modal');

modal.addEventListener('ty-modal-open', () => {
  // Track modal open
  analytics.track('Modal Opened', {
    modalId: 'user-profile',
    timestamp: new Date()
  });
  
  // Start a timer
  modal.openTime = Date.now();
});

modal.addEventListener('ty-modal-close', (e) => {
  // Calculate time spent
  const timeSpent = Date.now() - modal.openTime;
  
  // Track modal close
  analytics.track('Modal Closed', {
    modalId: 'user-profile',
    closeReason: e.detail.reason,
    timeSpent: timeSpent
  });
  
  // Cleanup
  delete modal.openTime;
});"
     "javascript")]

   ;; CSS Customization
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "CSS Customization"]
    [:p.ty-text-.mb-4 "Customize modal backdrop using CSS variables."]

    (code-block

     "/* Custom backdrop styles */
ty-modal {
  --ty-modal-backdrop: rgba(0, 50, 100, 0.8);
  --ty-modal-backdrop-blur: blur(8px);
  --ty-modal-duration: 300ms;
}

/* Different backdrop for specific modal */
#special-modal {
  --ty-modal-backdrop: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0.9),
    rgba(0, 0, 50, 0.7)
  );
  --ty-modal-backdrop-blur: blur(20px);
}"
     "css")

    [:div.mt-4
     [:ty-button {:on {:click #(swap! state/state assoc :docs-modal-custom-css true)}}
      "Open Custom Styled Modal"]

     [:ty-modal {:id "docs-modal-custom-css"
                 :open (get @state/state :docs-modal-custom-css false)
                 :style {"--ty-modal-backdrop" "linear-gradient(to bottom, rgba(59, 130, 246, 0.8), rgba(147, 51, 234, 0.8))"
                         "--ty-modal-backdrop-blur" "blur(12px)"
                         "--ty-modal-duration" "400ms"}
                 :on {:ty-modal-close #(swap! state/state assoc :docs-modal-custom-css false)}}
      [:div.p-8.max-w-md.ty-floating.rounded-xl.shadow-2xl
       [:h3.text-xl.font-semibold.mb-4.ty-text "Custom Backdrop"]
       [:p.ty-text- "This modal has a gradient backdrop with increased blur."]
       [:ty-button.mt-4 {:flavor "primary"
                         :on {:click #(swap! state/state assoc :docs-modal-custom-css false)}}
        "Beautiful!"]]]]]

   ;; Common Use Cases
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Common Use Cases"]
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     [:div
      [:h4.font-medium.ty-text.mb-2 "Confirmation Dialogs"]
      [:p.ty-text-.text-sm "Delete confirmations, logout warnings, discard changes prompts"]]
     [:div
      [:h4.font-medium.ty-text.mb-2 "Form Dialogs"]
      [:p.ty-text-.text-sm "Create/edit forms, settings panels, data entry"]]
     [:div
      [:h4.font-medium.ty-text.mb-2 "Content Display"]
      [:p.ty-text-.text-sm "Image galleries, video players, article previews"]]
     [:div
      [:h4.font-medium.ty-text.mb-2 "Wizards & Flows"]
      [:p.ty-text-.text-sm "Multi-step processes, onboarding, guided tours"]]
     [:div
      [:h4.font-medium.ty-text.mb-2 "Notifications"]
      [:p.ty-text-.text-sm "Important alerts, system messages, updates"]]
     [:div
      [:h4.font-medium.ty-text.mb-2 "Contextual Help"]
      [:p.ty-text-.text-sm "Tooltips, help panels, documentation viewers"]]]]

;; Advanced Patterns
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Advanced Patterns"]

   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "Modal Manager Pattern"]
    [:p.ty-text-.mb-4 "Manage multiple modals with a simple JavaScript controller."]

    (code-block
     "// Modal manager for handling multiple modals
class ModalManager {
  constructor() {
    this.activeModals = new Set();
  }
  
  open(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
      modal.show();
      this.activeModals.add(modalId);
      
      // Track modal state
      modal.addEventListener('ty-modal-close', () => {
        this.activeModals.delete(modalId);
      }, { once: true });
    }
  }
  
  close(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
      modal.hide();
      this.activeModals.delete(modalId);
    }
  }
  
  closeAll() {
    this.activeModals.forEach(id => this.close(id));
  }
  
  isOpen(modalId) {
    return this.activeModals.has(modalId);
  }
}

// Usage
const modals = new ModalManager();
modals.open('user-settings');
modals.open('confirm-dialog');
modals.closeAll(); // Close all open modals"
     "javascript")]

   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "Dynamic Content Loading"]
    [:p.ty-text-.mb-4 "Load modal content dynamically from server or templates."]

    (code-block
     "// Dynamic modal content loading
async function openUserProfile(userId) {
  const modal = document.getElementById('profile-modal');
  const content = modal.querySelector('.modal-body');
  
  // Show loading state
  content.innerHTML = '<div class=\"loading\">Loading...</div>';
  modal.show();
  
  try {
    // Fetch user data
    const response = await fetch(`/api/users/${userId}`);
    const user = await response.json();
    
    // Update modal content
    content.innerHTML = `
      <div class=\"user-profile\">
        <img src=\"${user.avatar}\" alt=\"${user.name}\">
        <h3>${user.name}</h3>
        <p>${user.bio}</p>
        <div class=\"stats\">
          <span>Posts: ${user.posts}</span>
          <span>Followers: ${user.followers}</span>
        </div>
      </div>
    `;
  } catch (error) {
    content.innerHTML = '<div class=\"error\">Failed to load profile</div>';
  }
}

// HTML
// <ty-modal id=\"profile-modal\">
//   <div class=\"ty-elevated rounded-lg p-6\">
//     <div class=\"modal-body\"></div>
//     <ty-button onclick=\"this.closest('ty-modal').hide()\">Close</ty-button>
//   </div>
// </ty-modal>"
     "javascript")]

   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text.mb-3 "Confirmation Dialog Pattern"]
    [:p.ty-text-.mb-4 "Create reusable confirmation dialogs with promises."]

    (code-block
     "// Reusable confirmation dialog
function confirm(message, options = {}) {
  return new Promise((resolve) => {
    const modal = document.getElementById('confirm-modal');
    const messageEl = modal.querySelector('.message');
    const confirmBtn = modal.querySelector('.confirm-btn');
    const cancelBtn = modal.querySelector('.cancel-btn');
    
    // Set message and options
    messageEl.textContent = message;
    confirmBtn.textContent = options.confirmText || 'Confirm';
    cancelBtn.textContent = options.cancelText || 'Cancel';
    
    // Set button colors based on action type
    if (options.danger) {
      confirmBtn.setAttribute('flavor', 'danger');
    } else {
      confirmBtn.setAttribute('flavor', 'primary');
    }
    
    // Handle user response
    const handleConfirm = () => {
      cleanup();
      resolve(true);
    };
    
    const handleCancel = () => {
      cleanup();
      resolve(false);
    };
    
    const cleanup = () => {
      modal.hide();
      confirmBtn.removeEventListener('click', handleConfirm);
      cancelBtn.removeEventListener('click', handleCancel);
    };
    
    // Attach listeners
    confirmBtn.addEventListener('click', handleConfirm);
    cancelBtn.addEventListener('click', handleCancel);
    
    // Show modal
    modal.show();
  });
}

// Usage
async function deleteItem(itemId) {
  const confirmed = await confirm(
    'Are you sure you want to delete this item?',
    { danger: true, confirmText: 'Delete', cancelText: 'Keep' }
  );
  
  if (confirmed) {
    await fetch(`/api/items/${itemId}`, { method: 'DELETE' });
    console.log('Item deleted');
  }
}"
     "javascript")]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6
    [:h2.text-xl.font-semibold.ty-text.mb-4 "Best Practices"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     [:div
      [:h4.font-medium.ty-text-success.flex.items-center.gap-2.mb-3
       [:ty-icon {:name "check-circle"}]
       "Do's"]
      [:ul.space-y-2.text-sm.ty-text-
       [:li "• Apply all styling to your content, not the modal"]
       [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "ty-elevated"] " or " [:code.ty-bg-neutral-.px-1.rounded "ty-floating"] " for modal content"]
       [:li "• Include a clear close action (button or X)"]
       [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "protected"] " mode for forms with unsaved changes"]
       [:li "• Handle the " [:code.ty-bg-neutral-.px-1.rounded "ty-modal-close"] " event for cleanup"]
       [:li "• Set appropriate " [:code.ty-bg-neutral-.px-1.rounded "max-width"] " on content"]
       [:li "• Use semantic HTML in modal content"]
       [:li "• Test keyboard navigation (Tab, Esc)"]]]

     [:div
      [:h4.font-medium.ty-text-danger.flex.items-center.gap-2.mb-3
       [:ty-icon {:name "x-circle"}]
       "Don'ts"]
      [:ul.space-y-2.text-sm.ty-text-
       [:li "• Don't style the " [:code.ty-bg-neutral-.px-1.rounded "ty-modal"] " element directly"]
       [:li "• Don't forget focus management for keyboard users"]
       [:li "• Don't nest modals unless absolutely necessary"]
       [:li "• Don't use modals for trivial confirmations"]
       [:li "• Don't make modals too large or complex"]
       [:li "• Don't auto-open modals without user action"]
       [:li "• Don't forget to handle cleanup on close"]
       [:li "• Don't ignore mobile responsiveness"]]]]]])
