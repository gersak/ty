import React, { useState, useRef } from 'react'
import { TyButton, TyTag, TyInput, TyInputEventDetail, TyDropdown, TyOption, TyDropdownEventDetail, OptionData, TyIcon, TyModal, TyModalRef, TyModalEventDetail, TyTooltip, TyMultiselect, TyMultiselectEventDetail, TyCalendar, TyCalendarChangeEventDetail, TyCalendarNavigateEventDetail, TyDatePicker, TyDatePickerEventDetail, TyPopup } from './components'

function App() {
  const [clickCount, setClickCount] = useState(0);
  const [lastClicked, setLastClicked] = useState('');
  const [tagEvents, setTagEvents] = useState<string[]>([]);

  // Input states
  const [inputValues, setInputValues] = useState({
    text: '',
    email: '',
    number: '',
    currency: '',
    password: ''
  });
  const [inputEvents, setInputEvents] = useState<string[]>([]);

  // Dropdown states
  const [dropdownValues, setDropdownValues] = useState({
    fruits: '',
    colors: '',
    sizes: '',
    language: 'clojure',
    team: '',
    environment: '',
    isolated1: '',
    isolated2: '',
    countries: '',
    countries2: ''
  });
  const [dropdownEvents, setDropdownEvents] = useState<string[]>([]);

  // Multiselect states
  const [multiselectValues, setMultiselectValues] = useState({
    skills: [] as string[],
    technologies: [] as string[],
    colors: [] as string[],
    categories: [] as string[]
  });
  const [multiselectEvents, setMultiselectEvents] = useState<string[]>([]);

  // Calendar states
  const [calendarValues, setCalendarValues] = useState({
    basicCalendar: { year: 2025, month: 1, day: 15 },
    formCalendar: { year: undefined as number | undefined, month: undefined as number | undefined, day: undefined as number | undefined },
    customCalendar: { year: 2025, month: 3, day: undefined as number | undefined }
  });
  const [calendarEvents, setCalendarEvents] = useState<string[]>([]);

  // DatePicker states
  const [datePickerValues, setDatePickerValues] = useState({
    basicDatePicker: '',
    formDatePicker: '',
    timePickerExample: '',
    rangeExample: { start: '', end: '' }
  });
  const [datePickerEvents, setDatePickerEvents] = useState<string[]>([]);

  // Popup states
  const [popupStates, setPopupStates] = useState({
    basicPopup: false,
    tooltipPopup: false,
    contextMenuPopup: false,
    dropdownPopup: false,
    positionTests: {
      top: false,
      bottom: false,
      left: false,
      right: false
    }
  });

  // Modal states  
  const [modalStates, setModalStates] = useState({
    basic: false,
    withDropdown: false,
    complex: false,
    protected: false,
    noBackdrop: false,
    controlled: false
  });
  const [modalEvents, setModalEvents] = useState<string[]>([]);

  // Modal refs for imperative control
  const basicModalRef = useRef<TyModalRef>(null);
  const controlledModalRef = useRef<TyModalRef>(null);

  // Sample data for data-driven dropdowns
  const fruitOptions: OptionData[] = [
    { value: 'apple', text: 'Apple' },
    { value: 'banana', text: 'Banana' },
    { value: 'cherry', text: 'Cherry' },
    { value: 'date', text: 'Date' },
    { value: 'elderberry', text: 'Elderberry' }
  ];

  const handleButtonClick = (flavor: string) => {
    setClickCount(prev => prev + 1);
    setLastClicked(flavor);
    console.log(`${flavor} button clicked!`);
  };

  const handleTagClick = (tagValue: string) => {
    const event = `Tag clicked: ${tagValue}`;
    setTagEvents(prev => [event, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(event);
  };

  const handleTagDismiss = (tagValue: string) => {
    const event = `Tag dismissed: ${tagValue}`;
    setTagEvents(prev => [event, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(event);
  };

  const handleInputChange = (inputType: string, event: CustomEvent<TyInputEventDetail>) => {
    const { value, formattedValue, rawValue } = event.detail;

    // Update state with the processed value
    setInputValues(prev => ({
      ...prev,
      [inputType]: value
    }));

    // Log event details
    const eventLog = `${inputType}: raw="${rawValue}" → value=${JSON.stringify(value)} → formatted="${formattedValue}"`;
    setInputEvents(prev => [eventLog, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(`Input change - ${eventLog}`);
  };

  const handleDropdownChange = (dropdownType: string, event: CustomEvent<TyDropdownEventDetail>) => {
    const option = event.detail.option;
    const value = option.getAttribute('value') || option.textContent || '';
    const text = option.textContent || '';

    // Update state with the selected value
    setDropdownValues(prev => ({
      ...prev,
      [dropdownType]: value
    }));

    // Log event details
    const eventLog = `${dropdownType}: selected "${text}" (value: ${value})`;
    setDropdownEvents(prev => [eventLog, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(`Dropdown change - ${eventLog}`);
  };

  const handleMultiselectChange = (multiselectType: string, event: CustomEvent<TyMultiselectEventDetail>) => {
    const { values, action, item } = event.detail;

    // Update state with the new values array
    setMultiselectValues(prev => ({
      ...prev,
      [multiselectType]: values
    }));

    // Log event details
    const eventLog = `${multiselectType}: ${action} "${item}" → [${values.join(', ')}] (${values.length} selected)`;
    setMultiselectEvents(prev => [eventLog, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(`Multiselect change - ${eventLog}`);
  };

  const handleCalendarChange = (calendarType: string, event: CustomEvent<TyCalendarChangeEventDetail>) => {
    const { year, month, day, action, source } = event.detail;

    // Update state with the selected date
    setCalendarValues(prev => ({
      ...prev,
      [calendarType]: { year, month, day }
    }));

    // Log event details
    const eventLog = `${calendarType}: ${action} ${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} (${source})`;
    setCalendarEvents(prev => [eventLog, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(`Calendar change - ${eventLog}`);
  };

  const handleCalendarNavigate = (calendarType: string, event: CustomEvent<TyCalendarNavigateEventDetail>) => {
    const { year, month, action, source } = event.detail;

    // Log navigation details
    const eventLog = `${calendarType}: ${action} to ${year}-${month.toString().padStart(2, '0')} (${source})`;
    setCalendarEvents(prev => [eventLog, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(`Calendar navigate - ${eventLog}`);
  };

  const handleDatePickerChange = (datePickerType: string, event: CustomEvent<TyDatePickerEventDetail>) => {
    const { value, milliseconds, source, formatted } = event.detail;

    // Update state with the selected value
    setDatePickerValues(prev => ({
      ...prev,
      [datePickerType]: value || ''
    }));

    // Log event details
    const eventLog = `${datePickerType}: ${source} → "${value}" (formatted: "${formatted}")`;
    setDatePickerEvents(prev => [eventLog, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(`DatePicker change - ${eventLog}`);
  };

  const handleDatePickerOpen = (datePickerType: string, event: CustomEvent<{}>) => {
    const eventLog = `${datePickerType}: opened`;
    setDatePickerEvents(prev => [eventLog, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(`DatePicker open - ${eventLog}`);
  };

  const handleDatePickerClose = (datePickerType: string, event: CustomEvent<{}>) => {
    const eventLog = `${datePickerType}: closed`;
    setDatePickerEvents(prev => [eventLog, ...prev.slice(0, 4)]); // Keep last 5 events
    console.log(`DatePicker close - ${eventLog}`);
  };

  const togglePopup = (popupType: keyof typeof popupStates | string) => {
    if (popupType.includes('.')) {
      // Handle nested states like 'positionTests.top'
      const [parent, child] = popupType.split('.');
      setPopupStates(prev => ({
        ...prev,
        [parent]: {
          ...prev[parent as keyof typeof prev],
          [child]: !prev[parent as keyof typeof prev][child as keyof typeof prev[typeof parent]]
        }
      }));
    } else {
      setPopupStates(prev => ({ 
        ...prev, 
        [popupType]: !prev[popupType as keyof typeof prev] 
      }));
    }
  };

  const closeAllPopups = () => {
    setPopupStates({
      basicPopup: false,
      tooltipPopup: false,
      contextMenuPopup: false,
      dropdownPopup: false,
      positionTests: {
        top: false,
        bottom: false,
        left: false,
        right: false
      }
    });
  };

  const handleModalOpen = (modalType: string, event: CustomEvent<TyModalEventDetail>) => {
    const eventLog = `${modalType}: opened (reason: ${event.detail.reason || 'unknown'})`;
    setModalEvents(prev => [eventLog, ...prev.slice(0, 9)]); // Keep last 10 events
    console.log(`Modal open - ${eventLog}`);
  };

  const handleModalClose = (modalType: string, event: CustomEvent<TyModalEventDetail>) => {
    const { reason, returnValue } = event.detail;
    const eventLog = `${modalType}: closed (reason: ${reason || 'unknown'}${returnValue ? `, returnValue: ${returnValue}` : ''})`;
    setModalEvents(prev => [eventLog, ...prev.slice(0, 9)]); // Keep last 10 events
    console.log(`Modal close - ${eventLog}`);

    // Update modal state when modal closes
    setModalStates(prev => ({
      ...prev,
      [modalType]: false
    }));
  };

  const openModal = (modalType: keyof typeof modalStates) => {
    setModalStates(prev => ({ ...prev, [modalType]: true }));
  };

  const closeModal = (modalType: keyof typeof modalStates) => {
    setModalStates(prev => ({ ...prev, [modalType]: false }));
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'system-ui, sans-serif', background: 'var(--ty-surface-canvas, #fafafa)' }}>
      <h1>🚀 Ty Components React Wrapper Tests</h1>

      {/* ICON TESTING SECTION */}
      <div className="demo-section" style={{
        border: '3px solid #10b981',
        padding: '2rem',
        marginBottom: '2rem',
        borderRadius: '12px',
        background: 'linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%)'
      }}>
        <h2 style={{ color: '#065f46', marginTop: 0 }}>🎨 TyIcon Component Tests</h2>

        <div style={{
          padding: '1.5rem',
          background: '#fef3c7',
          border: '2px solid #f59e0b',
          borderRadius: '8px',
          marginBottom: '2rem'
        }}>
          <h3 style={{ color: '#92400e', marginTop: 0 }}>✨ Icon Features Demo</h3>
          <p style={{ color: '#92400e', margin: 0 }}>
            Testing the TyIcon React wrapper with various sizes, animations, and styling options.
          </p>
        </div>

        {/* Icon Size Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📏 Icon Sizes</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
            Icons support both relative (em-based) and absolute (pixel-based) sizing.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '2rem', alignItems: 'end' }}>
            {/* Relative sizes */}
            <div>
              <h4 style={{ margin: '0 0 1rem 0', fontSize: '16px', color: '#374151' }}>Relative Sizes (em-based)</h4>
              <div style={{ display: 'flex', gap: '1rem', alignItems: 'end' }}>
                {['xs', 'sm', 'md', 'lg', 'xl', '2xl'].map(size => (
                  <div key={size} style={{ textAlign: 'center' }}>
                    <TyIcon name="star" size={size as any} />
                    <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '4px' }}>{size}</div>
                  </div>
                ))}
              </div>
            </div>

            {/* Absolute sizes */}
            <div>
              <h4 style={{ margin: '0 0 1rem 0', fontSize: '16px', color: '#374151' }}>Absolute Sizes (pixel-based)</h4>
              <div style={{ display: 'flex', gap: '1rem', alignItems: 'end' }}>
                {['16', '20', '24', '32', '48'].map(size => (
                  <div key={size} style={{ textAlign: 'center' }}>
                    <TyIcon name="heart" size={size as any} />
                    <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '4px' }}>{size}px</div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>

        {/* Animation Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🎭 Icon Animations</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
            Icons support spin and pulse animations with configurable tempo.
          </p>

          <div style={{ display: 'grid', gap: '2rem', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))' }}>
            {/* Spin animations */}
            <div>
              <h4 style={{ margin: '0 0 1rem 0', fontSize: '16px', color: '#374151' }}>Spin Animation</h4>
              <div style={{ display: 'flex', gap: '1.5rem', alignItems: 'center' }}>
                <div style={{ textAlign: 'center' }}>
                  <TyIcon name="settings" size="xl" spin />
                  <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '4px' }}>Normal</div>
                </div>
                <div style={{ textAlign: 'center' }}>
                  <TyIcon name="loader" size="xl" spin tempo="slow" />
                  <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '4px' }}>Slow</div>
                </div>
                <div style={{ textAlign: 'center' }}>
                  <TyIcon name="rotate-cw" size="xl" spin tempo="fast" />
                  <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '4px' }}>Fast</div>
                </div>
              </div>
            </div>

            {/* Pulse animations */}
            <div>
              <h4 style={{ margin: '0 0 1rem 0', fontSize: '16px', color: '#374151' }}>Pulse Animation</h4>
              <div style={{ display: 'flex', gap: '1.5rem', alignItems: 'center' }}>
                <div style={{ textAlign: 'center' }}>
                  <TyIcon name="heart" size="xl" pulse />
                  <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '4px' }}>Normal</div>
                </div>
                <div style={{ textAlign: 'center' }}>
                  <TyIcon name="zap" size="xl" pulse tempo="slow" />
                  <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '4px' }}>Slow</div>
                </div>
                <div style={{ textAlign: 'center' }}>
                  <TyIcon name="bell" size="xl" pulse tempo="fast" />
                  <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '4px' }}>Fast</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Color Inheritance Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🎨 Color Inheritance</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
            Icons inherit their color from parent elements using CSS currentColor.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '2rem' }}>
            <div style={{ color: 'var(--ty-color-success, #10b981)', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <TyIcon name="check-circle" size="xl" />
              <span style={{ fontSize: '16px', fontWeight: '500' }}>Success State</span>
            </div>
            <div style={{ color: 'var(--ty-color-danger, #ef4444)', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <TyIcon name="x-circle" size="xl" />
              <span style={{ fontSize: '16px', fontWeight: '500' }}>Error State</span>
            </div>
            <div style={{ color: 'var(--ty-color-warning, #f59e0b)', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <TyIcon name="alert-triangle" size="xl" />
              <span style={{ fontSize: '16px', fontWeight: '500' }}>Warning State</span>
            </div>
            <div style={{ color: 'var(--ty-color-info, #06b6d4)', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <TyIcon name="info" size="xl" />
              <span style={{ fontSize: '16px', fontWeight: '500' }}>Info State</span>
            </div>
            <div style={{ color: 'var(--ty-color-primary, #3b82f6)', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <TyIcon name="star" size="xl" />
              <span style={{ fontSize: '16px', fontWeight: '500' }}>Primary Action</span>
            </div>
          </div>
        </div>

        {/* Integration with Buttons */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🔗 Integration with Other Components</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
            Icons work seamlessly with other Ty components like buttons and inputs.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
            <TyButton flavor="primary" onClick={() => handleButtonClick('save')}>
              <TyIcon name="save" style={{ marginRight: '8px' }} />
              Save Changes
            </TyButton>

            <TyButton flavor="success" onClick={() => handleButtonClick('add')}>
              <TyIcon name="plus" style={{ marginRight: '8px' }} />
              Add Item
            </TyButton>

            <TyButton flavor="danger" onClick={() => handleButtonClick('delete')}>
              <TyIcon name="trash-2" style={{ marginRight: '8px' }} />
              Delete
            </TyButton>

            <TyButton flavor="warning" onClick={() => handleButtonClick('warning')}>
              <TyIcon name="alert-triangle" style={{ marginRight: '8px' }} />
              Warning Action
            </TyButton>

            <TyButton flavor="neutral" onClick={() => handleButtonClick('refresh')}>
              <TyIcon name="refresh-cw" style={{ marginRight: '8px' }} spin />
              Refresh
            </TyButton>
          </div>
        </div>

        {/* Icon Gallery */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🖼️ Icon Gallery</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
            Sample of available icons from the icon registry.
          </p>

          <div style={{ display: 'grid', gap: '1rem', gridTemplateColumns: 'repeat(auto-fill, minmax(120px, 1fr))' }}>
            {[
              'home', 'user', 'settings', 'search', 'mail', 'phone', 'calendar', 'clock',
              'file', 'folder', 'download', 'upload', 'share', 'bookmark', 'tag', 'flag',
              'camera', 'image', 'video', 'music', 'volume-2', 'wifi', 'bluetooth', 'battery',
              'sun', 'moon', 'cloud', 'umbrella', 'map-pin', 'navigation', 'compass', 'globe'
            ].map(iconName => (
              <div key={iconName} style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                padding: '12px',
                borderRadius: '6px',
                border: '1px solid #e5e7eb',
                transition: 'all 0.2s',
                cursor: 'pointer'
              }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.background = '#f3f4f6';
                  e.currentTarget.style.transform = 'scale(1.05)';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.background = 'transparent';
                  e.currentTarget.style.transform = 'scale(1)';
                }}
                onClick={() => {
                  navigator.clipboard.writeText(`<TyIcon name="${iconName}" />`);
                  console.log(`Copied: <TyIcon name="${iconName}" />`);
                }}>
                <TyIcon name={iconName} size="xl" />
                <span style={{ fontSize: '11px', color: '#6b7280', marginTop: '4px', textAlign: 'center' }}>
                  {iconName}
                </span>
              </div>
            ))}
          </div>

          <p style={{ fontSize: '12px', color: '#9ca3af', marginTop: '1rem', textAlign: 'center' }}>
            💡 Click any icon to copy its React code to clipboard
          </p>
        </div>

        {/* Status Display */}
        <div style={{
          padding: '1rem',
          background: '#dcfce7',
          border: '1px solid #16a34a',
          borderRadius: '8px'
        }}>
          <h4 style={{ color: '#15803d', marginTop: 0 }}>✅ TyIcon Implementation Status</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px' }}>
            <div>✅ <strong>TypeScript Interface:</strong> Complete with all props and sizes</div>
            <div>✅ <strong>Ref Forwarding:</strong> Proper ref handling for React integration</div>
            <div>✅ <strong>Attribute Mapping:</strong> React props → Web Component attributes</div>
            <div>✅ <strong>Boolean Attributes:</strong> Correct handling of spin/pulse flags</div>
            <div>✅ <strong>Size Variants:</strong> Both relative (em) and absolute (px) sizing</div>
            <div>✅ <strong>Animation Support:</strong> Spin/pulse with tempo control</div>
            <div>✅ <strong>Color Inheritance:</strong> Automatic currentColor usage</div>
            <div>✅ <strong>Component Integration:</strong> Works with buttons and other components</div>
          </div>
        </div>
      </div>

      {/* MODAL TESTING SECTION */}
      <div className="demo-section" style={{
        border: '3px solid #8b5cf6',
        padding: '2rem',
        marginBottom: '2rem',
        borderRadius: '12px',
        background: 'linear-gradient(135deg, #faf5ff 0%, #f3e8ff 100%)'
      }}>
        <h2 style={{ color: '#6b21a8', marginTop: 0 }}>🎭 TyModal Component Tests</h2>

        <div style={{
          padding: '1.5rem',
          background: '#fef3c7',
          border: '2px solid #f59e0b',
          borderRadius: '8px',
          marginBottom: '2rem'
        }}>
          <h3 style={{ color: '#92400e', marginTop: 0 }}>✨ Modal Features Demo</h3>
          <p style={{ color: '#92400e', margin: 0 }}>
            Testing the TyModal React wrapper with various configurations, event handling, and interaction patterns.
          </p>
        </div>

        {/* Basic Modal Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📱 Basic Modal Functionality</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
            Test core modal features including open/close, backdrop handling, and event dispatching.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem', marginBottom: '1.5rem' }}>
            <TyButton flavor="primary" onClick={() => openModal('basic')}>
              <TyIcon name="info" style={{ marginRight: '8px' }} />
              Basic Modal
            </TyButton>

            <TyButton flavor="secondary" onClick={() => openModal('noBackdrop')}>
              <TyIcon name="eye" style={{ marginRight: '8px' }} />
              No Backdrop
            </TyButton>

            <TyButton flavor="warning" onClick={() => openModal('protected')}>
              <TyIcon name="shield" style={{ marginRight: '8px' }} />
              Protected Modal
            </TyButton>
          </div>

          {/* Basic Modal */}
          <TyModal
            open={modalStates.basic}
            onOpen={(e) => handleModalOpen('basic', e)}
            onClose={(e) => handleModalClose('basic', e)}
          >
            <div style={{
              background: 'white',
              padding: '2rem',
              borderRadius: '12px',
              boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
              maxWidth: '500px',
              border: '1px solid #e5e7eb'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem' }}>
                <TyIcon name="info" size="lg" style={{ marginRight: '12px', color: 'var(--ty-color-primary)' }} />
                <h3 style={{ margin: 0, fontSize: '20px', fontWeight: '600' }}>Basic Modal</h3>
              </div>
              <p style={{ color: '#6b7280', marginBottom: '1.5rem' }}>
                This is a basic modal with standard backdrop and close behaviors.
                You can close it by clicking the X button, pressing Escape, or clicking outside.
              </p>
              <div style={{ display: 'flex', gap: '0.5rem', justifyContent: 'flex-end' }}>
                <TyButton flavor="neutral" onClick={() => closeModal('basic')}>
                  Cancel
                </TyButton>
                <TyButton flavor="primary" onClick={() => closeModal('basic')}>
                  <TyIcon name="check" style={{ marginRight: '8px' }} />
                  Confirm
                </TyButton>
              </div>
            </div>
          </TyModal>

          {/* No Backdrop Modal */}
          <TyModal
            open={modalStates.noBackdrop}
            backdrop={false}
            onOpen={(e) => handleModalOpen('noBackdrop', e)}
            onClose={(e) => handleModalClose('noBackdrop', e)}
          >
            <div style={{
              background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              color: 'white',
              padding: '2rem',
              borderRadius: '12px',
              boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.3)',
              maxWidth: '400px',
              border: '2px solid rgba(255, 255, 255, 0.2)'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem' }}>
                <TyIcon name="eye" size="lg" style={{ marginRight: '12px' }} />
                <h3 style={{ margin: 0, fontSize: '20px', fontWeight: '600' }}>No Backdrop Modal</h3>
              </div>
              <p style={{ opacity: 0.9, marginBottom: '1.5rem' }}>
                This modal has no backdrop, so you can see and interact with the content behind it.
                Close it using the button or Escape key.
              </p>
              <TyButton
                style={{ background: 'rgba(255, 255, 255, 0.2)', border: '1px solid rgba(255, 255, 255, 0.3)' }}
                onClick={() => closeModal('noBackdrop')}
              >
                <TyIcon name="x" style={{ marginRight: '8px' }} />
                Close
              </TyButton>
            </div>
          </TyModal>

          {/* Protected Modal */}
          <TyModal
            open={modalStates.protected}
            protected={true}
            onOpen={(e) => handleModalOpen('protected', e)}
            onClose={(e) => handleModalClose('protected', e)}
          >
            <div style={{
              background: 'white',
              padding: '2rem',
              borderRadius: '12px',
              boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
              maxWidth: '550px',
              border: '2px solid #f59e0b'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem' }}>
                <TyIcon name="shield" size="lg" style={{ marginRight: '12px', color: '#f59e0b' }} />
                <h3 style={{ margin: 0, fontSize: '20px', fontWeight: '600', color: '#92400e' }}>Protected Modal</h3>
              </div>
              <div style={{
                background: '#fef3c7',
                border: '1px solid #fbbf24',
                borderRadius: '8px',
                padding: '1rem',
                marginBottom: '1.5rem'
              }}>
                <p style={{ margin: 0, color: '#92400e', fontSize: '14px' }}>
                  ⚠️ <strong>Protected Mode:</strong> This modal will show a confirmation dialog
                  when you try to close it (simulating unsaved changes).
                </p>
              </div>
              <p style={{ color: '#6b7280', marginBottom: '1.5rem' }}>
                Try closing this modal using Escape, backdrop click, or the close button.
                You'll see a browser confirmation dialog asking if you want to proceed.
              </p>
              <div style={{ display: 'flex', gap: '0.5rem', justifyContent: 'flex-end' }}>
                <TyButton flavor="neutral" onClick={() => closeModal('protected')}>
                  Try to Cancel
                </TyButton>
                <TyButton flavor="warning" onClick={() => closeModal('protected')}>
                  <TyIcon name="save" style={{ marginRight: '8px' }} />
                  Save & Close
                </TyButton>
              </div>
            </div>
          </TyModal>
        </div>

        {/* Advanced Modal Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🧩 Advanced Modal Features</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
            Test modal integration with other components and complex interaction patterns.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem', marginBottom: '1.5rem' }}>
            <TyButton flavor="success" onClick={() => openModal('withDropdown')}>
              <TyIcon name="settings" style={{ marginRight: '8px' }} />
              Modal with Dropdown
            </TyButton>

            <TyButton flavor="info" onClick={() => openModal('complex')}>
              <TyIcon name="settings" style={{ marginRight: '8px' }} />
              Complex Form Modal
            </TyButton>

            <TyButton flavor="secondary" onClick={() => basicModalRef.current?.show()}>
              <TyIcon name="code" style={{ marginRight: '8px' }} />
              Imperative Control
            </TyButton>
          </div>

          {/* Modal with Dropdown */}
          <TyModal
            open={modalStates.withDropdown}
            onOpen={(e) => handleModalOpen('withDropdown', e)}
            onClose={(e) => handleModalClose('withDropdown', e)}
          >
            <div style={{
              background: 'white',
              padding: '2rem',
              borderRadius: '12px',
              boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
              maxWidth: '600px',
              border: '1px solid #e5e7eb'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1.5rem' }}>
                <TyIcon name="settings" size="lg" style={{ marginRight: '12px', color: 'var(--ty-color-success)' }} />
                <h3 style={{ margin: 0, fontSize: '20px', fontWeight: '600' }}>Modal with Dropdown</h3>
              </div>

              <div style={{ marginBottom: '1.5rem' }}>
                <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                  Select your favorite programming language:
                </label>
                <TyDropdown
                  placeholder="Choose a language..."
                  value={dropdownValues.language}
                  onChange={(e) => handleDropdownChange('language', e)}
                  style={{ width: '100%' }}
                >
                  <TyOption value="javascript">JavaScript</TyOption>
                  <TyOption value="typescript">TypeScript</TyOption>
                  <TyOption value="python">Python</TyOption>
                  <TyOption value="java">Java</TyOption>
                  <TyOption value="clojure">Clojure</TyOption>
                  <TyOption value="rust">Rust</TyOption>
                  <TyOption value="go">Go</TyOption>
                  <TyOption value="kotlin">Kotlin</TyOption>
                  <TyOption value="swift">Swift</TyOption>
                  <TyOption value="csharp">C#</TyOption>
                </TyDropdown>
              </div>

              <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
                Current selection: <strong>{dropdownValues.language || 'None'}</strong>
              </p>

              <div style={{ display: 'flex', gap: '0.5rem', justifyContent: 'flex-end' }}>
                <TyButton flavor="neutral" onClick={() => closeModal('withDropdown')}>
                  Cancel
                </TyButton>
                <TyButton flavor="success" onClick={() => closeModal('withDropdown')}>
                  <TyIcon name="check" style={{ marginRight: '8px' }} />
                  Save Preferences
                </TyButton>
              </div>
            </div>
          </TyModal>

          {/* Complex Form Modal */}
          <TyModal
            open={modalStates.complex}
            onOpen={(e) => handleModalOpen('complex', e)}
            onClose={(e) => handleModalClose('complex', e)}
          >
            <div style={{
              background: 'white',
              padding: '2.5rem',
              borderRadius: '12px',
              boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
              maxWidth: '700px',
              border: '1px solid #e5e7eb',
              maxHeight: '80vh',
              overflow: 'auto'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '2rem' }}>
                <TyIcon name="settings" size="xl" style={{ marginRight: '12px', color: 'var(--ty-color-info)' }} />
                <h3 style={{ margin: 0, fontSize: '24px', fontWeight: '600' }}>Project Settings</h3>
              </div>

              <div style={{ display: 'grid', gap: '1.5rem', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem' }}>
                    Project Name
                  </label>
                  <TyInput
                    placeholder="Enter project name..."
                    value="My Awesome Project"
                    style={{ width: '100%' }}
                  />
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem' }}>
                    Project Type
                  </label>
                  <TyDropdown placeholder="Select type..." flavor="info" style={{ width: '100%' }}>
                    <TyOption value="web">🌐 Web Application</TyOption>
                    <TyOption value="mobile">📱 Mobile App</TyOption>
                    <TyOption value="desktop">💻 Desktop Application</TyOption>
                    <TyOption value="api">🔌 API Service</TyOption>
                    <TyOption value="library">📚 Library/Package</TyOption>
                  </TyDropdown>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '0.5rem', justifyContent: 'flex-end', marginTop: '2rem', paddingTop: '1rem', borderTop: '1px solid #e5e7eb' }}>
                <TyButton flavor="neutral" onClick={() => closeModal('complex')}>
                  <TyIcon name="x" style={{ marginRight: '8px' }} />
                  Cancel
                </TyButton>
                <TyButton flavor="primary" onClick={() => closeModal('complex')}>
                  <TyIcon name="save" style={{ marginRight: '8px' }} />
                  Save Project
                </TyButton>
              </div>
            </div>
          </TyModal>

          {/* Imperative Control Modal */}
          <TyModal
            ref={basicModalRef}
            onOpen={(e) => handleModalOpen('imperative', e)}
            onClose={(e) => handleModalClose('imperative', e)}
          >
            <div style={{
              background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              color: 'white',
              padding: '2rem',
              borderRadius: '12px',
              boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.3)',
              maxWidth: '500px',
              border: '2px solid rgba(255, 255, 255, 0.2)'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem' }}>
                <TyIcon name="code" size="lg" style={{ marginRight: '12px' }} />
                <h3 style={{ margin: 0, fontSize: '20px', fontWeight: '600' }}>Imperative Control</h3>
              </div>
              <p style={{ opacity: 0.9, marginBottom: '1.5rem' }}>
                This modal was opened using the imperative <code style={{ background: 'rgba(0,0,0,0.3)', padding: '2px 6px', borderRadius: '4px' }}>ref.show()</code> method,
                demonstrating programmatic control beyond React state.
              </p>
              <TyButton
                style={{ background: 'rgba(255, 255, 255, 0.2)', border: '1px solid rgba(255, 255, 255, 0.3)' }}
                onClick={() => basicModalRef.current?.hide()}
              >
                <TyIcon name="x" style={{ marginRight: '8px' }} />
                Hide via ref.hide()
              </TyButton>
            </div>
          </TyModal>
        </div>

        {/* Event Log Section */}
        <div style={{
          padding: '1rem',
          background: '#f8fafc',
          border: '1px solid #e2e8f0',
          borderRadius: '8px',
          marginBottom: '1rem'
        }}>
          <h4 style={{ color: '#1f2937', marginTop: 0, marginBottom: '1rem' }}>📋 Modal Event Log</h4>
          {modalEvents.length > 0 ? (
            <div style={{ maxHeight: '200px', overflow: 'auto' }}>
              {modalEvents.map((event, index) => (
                <div key={index} style={{
                  fontSize: '12px',
                  color: '#6b7280',
                  padding: '4px 8px',
                  background: index % 2 === 0 ? 'white' : '#f8fafc',
                  borderRadius: '4px',
                  marginBottom: '2px',
                  fontFamily: 'monospace'
                }}>
                  {event}
                </div>
              ))}
            </div>
          ) : (
            <p style={{ fontSize: '14px', color: '#9ca3af', margin: 0 }}>
              No modal events yet. Try opening and closing modals to see event details!
            </p>
          )}
        </div>

        {/* Status Display */}
        <div style={{
          padding: '1rem',
          background: '#f0fdf4',
          border: '1px solid #16a34a',
          borderRadius: '8px'
        }}>
          <h4 style={{ color: '#15803d', marginTop: 0 }}>✅ TyModal Implementation Status</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px' }}>
            <div>✅ <strong>TypeScript Interface:</strong> Complete with event types and ref interface</div>
            <div>✅ <strong>Imperative Methods:</strong> show() and hide() via useImperativeHandle</div>
            <div>✅ <strong>Event Handling:</strong> ty-modal-open and ty-modal-close with detailed data</div>
            <div>✅ <strong>Attribute Mapping:</strong> React props → Web Component attributes with defaults</div>
            <div>✅ <strong>Boolean Props:</strong> Correct handling of backdrop, protected, close behaviors</div>
            <div>✅ <strong>Content Projection:</strong> Children rendered as modal content</div>
            <div>✅ <strong>Integration Testing:</strong> Works with dropdowns, inputs, and other components</div>
            <div>✅ <strong>Advanced Features:</strong> Protected mode, backdrop control, keyboard handling</div>
          </div>
        </div>
      </div>

      {/* TOOLTIP TESTING SECTION */}
      <div className="demo-section" style={{
        border: '3px solid #f59e0b',
        padding: '2rem',
        marginBottom: '2rem',
        borderRadius: '12px',
        background: 'linear-gradient(135deg, #fffbeb 0%, #fef3c7 100%)'
      }}>
        <h2 style={{ color: '#92400e', marginTop: 0 }}>💬 TyTooltip Component Tests</h2>

        <div style={{
          padding: '1.5rem',
          background: '#f0f9ff',
          border: '2px solid #0ea5e9',
          borderRadius: '8px',
          marginBottom: '2rem'
        }}>
          <h3 style={{ color: '#0c4a6e', marginTop: 0 }}>✨ Tooltip Features Demo</h3>
          <p style={{ color: '#0c4a6e', margin: 0 }}>
            Testing the TyTooltip React wrapper with various placements, flavors, delays, and content types. Hover over elements to see tooltips!
          </p>
        </div>

        {/* Basic Placement Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📍 Tooltip Placements</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test tooltip positioning in all four directions. The tooltip system automatically adjusts position based on viewport constraints.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem', justifyContent: 'center', alignItems: 'center', minHeight: '120px' }}>
            <TyButton flavor="primary">
              <TyIcon name="compass" style={{ marginRight: '8px' }} />
              Top Tooltip
              <TyTooltip placement="top">
                This tooltip appears above the button
              </TyTooltip>
            </TyButton>

            <TyButton flavor="secondary">
              <TyIcon name="compass" style={{ marginRight: '8px' }} />
              Bottom Tooltip
              <TyTooltip placement="bottom">
                This tooltip appears below the button
              </TyTooltip>
            </TyButton>

            <TyButton flavor="success">
              <TyIcon name="compass" style={{ marginRight: '8px' }} />
              Left Tooltip
              <TyTooltip placement="left">
                This tooltip appears to the left of the button
              </TyTooltip>
            </TyButton>

            <TyButton flavor="info">
              <TyIcon name="compass" style={{ marginRight: '8px' }} />
              Right Tooltip
              <TyTooltip placement="right">
                This tooltip appears to the right of the button
              </TyTooltip>
            </TyButton>
          </div>
        </div>

        {/* Flavor Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🎨 Tooltip Flavors</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test semantic tooltip flavors that match different UI contexts and message types.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
            <TyButton flavor="neutral">
              Dark (Default)
              <TyTooltip>
                Default dark tooltip with neutral styling
              </TyTooltip>
            </TyButton>

            <TyButton flavor="neutral" style={{ background: '#374151', color: 'white' }}>
              Light
              <TyTooltip flavor="light">
                Light tooltip for dark backgrounds
              </TyTooltip>
            </TyButton>

            <TyButton flavor="primary">
              Primary
              <TyTooltip flavor="primary">
                Primary tooltip for important information
              </TyTooltip>
            </TyButton>

            <TyButton flavor="secondary">
              Secondary
              <TyTooltip flavor="secondary">
                Secondary tooltip for supplementary info
              </TyTooltip>
            </TyButton>

            <TyButton flavor="success">
              Success
              <TyTooltip flavor="success">
                <TyIcon name="check" style={{ marginRight: '6px' }} />
                Success tooltip for positive feedback
              </TyTooltip>
            </TyButton>

            <TyButton flavor="danger">
              Danger
              <TyTooltip flavor="danger">
                <TyIcon name="alert-triangle" style={{ marginRight: '6px' }} />
                Danger tooltip for warnings
              </TyTooltip>
            </TyButton>

            <TyButton flavor="warning">
              Warning
              <TyTooltip flavor="warning">
                <TyIcon name="alert-triangle" style={{ marginRight: '6px' }} />
                Warning tooltip for caution
              </TyTooltip>
            </TyButton>

            <TyButton flavor="info">
              Info
              <TyTooltip flavor="info">
                <TyIcon name="info" style={{ marginRight: '6px' }} />
                Info tooltip for helpful details
              </TyTooltip>
            </TyButton>
          </div>
        </div>

        {/* Rich Content Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📝 Rich Content Tooltips</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test tooltips with complex content including text formatting, icons, and multiple elements.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
            <TyButton flavor="primary">
              <TyIcon name="file" style={{ marginRight: '8px' }} />
              Rich Text
              <TyTooltip flavor="primary">
                <div>
                  <strong>Bold text</strong> and <em>italic text</em>
                  <br />
                  With line breaks and formatting
                </div>
              </TyTooltip>
            </TyButton>

            <TyButton flavor="info">
              <TyIcon name="list" style={{ marginRight: '8px' }} />
              Multi-line
              <TyTooltip flavor="info" placement="bottom">
                <div>
                  <div style={{ fontWeight: '600' }}>🚀 Features:</div>
                  <div style={{ fontSize: '13px', marginTop: '4px' }}>
                    • Rich content support
                    <br />
                    • Multiple lines
                    <br />
                    • Custom styling
                  </div>
                </div>
              </TyTooltip>
            </TyButton>

            <TyButton flavor="success">
              <TyIcon name="check" style={{ marginRight: '8px' }} />
              With Icons
              <TyTooltip flavor="success">
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <TyIcon name="check-circle" />
                  <span>Success with icon tooltip</span>
                </div>
              </TyTooltip>
            </TyButton>

            <TyButton flavor="warning">
              <TyIcon name="code" style={{ marginRight: '8px' }} />
              Code Example
              <TyTooltip flavor="warning" placement="top">
                <div style={{ fontFamily: 'monospace', fontSize: '12px' }}>
                  <div style={{ fontWeight: '600', marginBottom: '4px' }}>Usage:</div>
                  <code style={{ background: 'rgba(0,0,0,0.1)', padding: '2px 4px', borderRadius: '2px' }}>
                    &lt;TyTooltip&gt;content&lt;/TyTooltip&gt;
                  </code>
                </div>
              </TyTooltip>
            </TyButton>
          </div>
        </div>

        {/* Delay and Interaction Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>⏱️ Delay and Interaction</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test different show delays and interaction behaviors.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
            <TyButton flavor="info">
              <TyIcon name="zap" style={{ marginRight: '8px' }} />
              Quick (100ms)
              <TyTooltip delay={100}>
                This tooltip shows quickly after 100ms
              </TyTooltip>
            </TyButton>

            <TyButton flavor="primary">
              <TyIcon name="clock" style={{ marginRight: '8px' }} />
              Default (600ms)
              <TyTooltip>
                This tooltip shows after the default 600ms delay
              </TyTooltip>
            </TyButton>

            <TyButton flavor="warning">
              <TyIcon name="clock" style={{ marginRight: '8px' }} />
              Slow (1200ms)
              <TyTooltip delay={1200}>
                This tooltip shows slowly after 1200ms
              </TyTooltip>
            </TyButton>

            <TyButton flavor="neutral">
              <TyIcon name="x" style={{ marginRight: '8px' }} />
              Disabled
              <TyTooltip disabled>
                You won't see this tooltip
              </TyTooltip>
            </TyButton>
          </div>
        </div>

        {/* Different Element Types */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🎯 Different Element Types</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test tooltips on various UI elements and components.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem', alignItems: 'center' }}>
            {/* Icon with tooltip */}
            <div style={{ position: 'relative' }}>
              <TyIcon name="info" size="xl" style={{ color: 'var(--ty-color-primary)', cursor: 'help' }} />
              <TyTooltip flavor="primary" placement="top">
                Click for more information
              </TyTooltip>
            </div>

            {/* Tag with tooltip */}
            <TyTag flavor="success" clickable>
              Status: Active
              <TyTooltip flavor="success">
                <div>
                  <div style={{ fontWeight: '600' }}>Status Details:</div>
                  <div style={{ fontSize: '13px', marginTop: '4px' }}>
                    • System is operational
                    <br />
                    • All services running
                    <br />
                    • Last updated: 2 minutes ago
                  </div>
                </div>
              </TyTooltip>
            </TyTag>

            {/* Input with tooltip */}
            <div style={{ position: 'relative' }}>
              <TyInput placeholder="Enter value..." style={{ width: '150px' }} />
              <TyTooltip flavor="info" placement="top">
                <div>
                  <div style={{ fontWeight: '600' }}>💡 Pro tip:</div>
                  <div style={{ fontSize: '13px', marginTop: '4px' }}>
                    Use Ctrl+V to paste content
                  </div>
                </div>
              </TyTooltip>
            </div>

            {/* Custom div with tooltip */}
            <div style={{
              padding: '8px 12px',
              background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              color: 'white',
              borderRadius: '8px',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              gap: '6px'
            }}>
              <TyIcon name="star" />
              Custom Element
              <TyTooltip flavor="light" placement="bottom">
                This is a custom styled element with a tooltip
              </TyTooltip>
            </div>
          </div>
        </div>

        {/* Offset and Positioning Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📏 Offset and Positioning</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test custom offset distances and positioning behavior.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
            <TyButton flavor="primary">
              Close (2px)
              <TyTooltip offset={2} placement="top">
                Very close tooltip with 2px offset
              </TyTooltip>
            </TyButton>

            <TyButton flavor="secondary">
              Default (8px)
              <TyTooltip placement="top">
                Default tooltip with 8px offset
              </TyTooltip>
            </TyButton>

            <TyButton flavor="info">
              Far (20px)
              <TyTooltip offset={20} placement="top">
                Distant tooltip with 20px offset
              </TyTooltip>
            </TyButton>
          </div>
        </div>

        {/* Edge Case Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🧪 Edge Cases</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test edge cases like viewport boundaries and long content.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem', justifyContent: 'space-between' }}>
            {/* Left edge */}
            <TyButton flavor="warning">
              Left Edge
              <TyTooltip placement="left">
                This tooltip should flip to the right when near the left edge
              </TyTooltip>
            </TyButton>

            {/* Long content */}
            <TyButton flavor="info">
              Long Content
              <TyTooltip placement="top">
                This is a very long tooltip that demonstrates how the component handles text wrapping and maximum width constraints. The content should wrap properly and not exceed the configured maximum width.
              </TyTooltip>
            </TyButton>

            {/* Right edge */}
            <TyButton flavor="success">
              Right Edge
              <TyTooltip placement="right">
                This tooltip should flip to the left when near the right edge
              </TyTooltip>
            </TyButton>
          </div>
        </div>

        {/* Status Display */}
        <div style={{
          padding: '1rem',
          background: '#f0fdf4',
          border: '1px solid #16a34a',
          borderRadius: '8px'
        }}>
          <h4 style={{ color: '#15803d', marginTop: 0 }}>✅ TyTooltip Implementation Status</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px' }}>
            <div>✅ <strong>TypeScript Interface:</strong> Complete with all placement and flavor options</div>
            <div>✅ <strong>Ref Forwarding:</strong> Proper ref handling for React integration</div>
            <div>✅ <strong>Attribute Mapping:</strong> React props → Web Component attributes</div>
            <div>✅ <strong>Number Props:</strong> Correct handling of offset and delay as strings</div>
            <div>✅ <strong>Boolean Props:</strong> Proper disabled attribute handling</div>
            <div>✅ <strong>Content Projection:</strong> Children rendered as tooltip content</div>
            <div>✅ <strong>Semantic Flavors:</strong> All 9 flavor variants supported</div>
            <div>✅ <strong>Positioning System:</strong> Smart placement with viewport edge detection</div>
            <div>✅ <strong>Rich Content:</strong> Supports HTML, icons, and complex layouts</div>
            <div>✅ <strong>Integration Ready:</strong> Works with buttons, inputs, tags, and custom elements</div>
          </div>
        </div>
      </div>

      {/* MULTISELECT TESTING SECTION */}
      <div className="demo-section" style={{
        border: '3px solid #a855f7',
        padding: '2rem',
        marginBottom: '2rem',
        borderRadius: '12px',
        background: 'linear-gradient(135deg, #faf5ff 0%, #f3e8ff 100%)'
      }}>
        <h2 style={{ color: '#7c3aed', marginTop: 0 }}>🎯 TyMultiselect Component Tests</h2>

        <div style={{
          padding: '1.5rem',
          background: '#fef3c7',
          border: '2px solid #f59e0b',
          borderRadius: '8px',
          marginBottom: '2rem'
        }}>
          <h3 style={{ color: '#92400e', marginTop: 0 }}>✨ Multiselect Features Demo</h3>
          <p style={{ color: '#92400e', margin: 0 }}>
            Testing the TyMultiselect React wrapper with TyTag children (not TyOption), multiple selection patterns, event handling, form integration, and state management. Tags automatically move between slots when selected/deselected.
          </p>
        </div>

        {/* Basic Multiselect Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🏷️ Basic Multiselect Functionality</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test core multiselect features including multiple selection, tag management, and change events. Uses TyTag components that automatically move between "options" slot and "selected" slot based on selection state.
          </p>

          <div style={{ display: 'grid', gap: '2rem', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))' }}>
            {/* Skills Multiselect */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Select your skills:
              </label>
              <TyMultiselect
                placeholder="Choose your technical skills..."
                value={multiselectValues.skills}
                onChange={(e) => handleMultiselectChange('skills', e)}
                style={{ width: '100%' }}
              >
                <TyTag value="javascript">JavaScript</TyTag>
                <TyTag value="typescript">TypeScript</TyTag>
                <TyTag value="react">React</TyTag>
                <TyTag value="vue">Vue.js</TyTag>
                <TyTag value="angular">Angular</TyTag>
                <TyTag value="svelte">Svelte</TyTag>
                <TyTag value="node">Node.js</TyTag>
                <TyTag value="python">Python</TyTag>
                <TyTag value="java">Java</TyTag>
                <TyTag value="clojure">Clojure</TyTag>
                <TyTag value="rust">Rust</TyTag>
                <TyTag value="go">Go</TyTag>
                <TyTag value="kotlin">Kotlin</TyTag>
                <TyTag value="swift">Swift</TyTag>
              </TyMultiselect>

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                <strong>Selected ({multiselectValues.skills.length}):</strong>{' '}
                {multiselectValues.skills.length > 0 ? multiselectValues.skills.join(', ') : 'None'}
              </div>
            </div>

            {/* Technologies Multiselect */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Select technologies:
              </label>
              <TyMultiselect
                placeholder="Choose development technologies..."
                value={multiselectValues.technologies}
                flavor="primary"
                onChange={(e) => handleMultiselectChange('technologies', e)}
                style={{ width: '100%' }}
              >
                <TyTag value="docker">🐳 Docker</TyTag>
                <TyTag value="kubernetes">☸️ Kubernetes</TyTag>
                <TyTag value="aws">☁️ AWS</TyTag>
                <TyTag value="azure">🔵 Azure</TyTag>
                <TyTag value="gcp">🟡 Google Cloud</TyTag>
                <TyTag value="terraform">🏗️ Terraform</TyTag>
                <TyTag value="jenkins">🔧 Jenkins</TyTag>
                <TyTag value="github">🐙 GitHub Actions</TyTag>
                <TyTag value="gitlab">🦊 GitLab CI</TyTag>
                <TyTag value="redis">🔴 Redis</TyTag>
                <TyTag value="postgres">🐘 PostgreSQL</TyTag>
                <TyTag value="mongodb">🍃 MongoDB</TyTag>
                <TyTag value="elasticsearch">🔍 Elasticsearch</TyTag>
                <TyTag value="kafka">📨 Apache Kafka</TyTag>
              </TyMultiselect>

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                <strong>Selected ({multiselectValues.technologies.length}):</strong>{' '}
                {multiselectValues.technologies.length > 0 ? multiselectValues.technologies.join(', ') : 'None'}
              </div>
            </div>
          </div>
        </div>

        {/* Advanced Features */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🎨 Flavor Variations & States</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test semantic flavors, disabled states, and visual variations.
          </p>

          <div style={{ display: 'grid', gap: '2rem', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))' }}>
            {/* Color Multiselect with Success Flavor */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Pick your favorite colors:
              </label>
              <TyMultiselect
                placeholder="Select multiple colors..."
                value={multiselectValues.colors}
                flavor="success"
                onChange={(e) => handleMultiselectChange('colors', e)}
                style={{ width: '100%' }}
              >
                <TyTag value="red">🔴 Red</TyTag>
                <TyTag value="orange">🟠 Orange</TyTag>
                <TyTag value="yellow">🟡 Yellow</TyTag>
                <TyTag value="green">🟢 Green</TyTag>
                <TyTag value="blue">🔵 Blue</TyTag>
                <TyTag value="purple">🟣 Purple</TyTag>
                <TyTag value="pink">🩷 Pink</TyTag>
                <TyTag value="brown">🟤 Brown</TyTag>
                <TyTag value="black">⚫ Black</TyTag>
                <TyTag value="white">⚪ White</TyTag>
                <TyTag value="gray">🔘 Gray</TyTag>
              </TyMultiselect>

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                <strong>Colors ({multiselectValues.colors.length}):</strong>{' '}
                {multiselectValues.colors.length > 0 ? multiselectValues.colors.join(', ') : 'None'}
              </div>
            </div>

            {/* Categories with Warning Flavor */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Content categories:
              </label>
              <TyMultiselect
                placeholder="Select content categories..."
                value={multiselectValues.categories}
                flavor="warning"
                onChange={(e) => handleMultiselectChange('categories', e)}
                style={{ width: '100%' }}
              >
                <TyTag value="tech">💻 Technology</TyTag>
                <TyTag value="design">🎨 Design</TyTag>
                <TyTag value="business">💼 Business</TyTag>
                <TyTag value="marketing">📈 Marketing</TyTag>
                <TyTag value="education">📚 Education</TyTag>
                <TyTag value="health">🏥 Health & Wellness</TyTag>
                <TyTag value="travel">✈️ Travel</TyTag>
                <TyTag value="food">🍕 Food & Cooking</TyTag>
                <TyTag value="sports">⚽ Sports</TyTag>
                <TyTag value="music">🎵 Music</TyTag>
                <TyTag value="gaming">🎮 Gaming</TyTag>
                <TyTag value="science">🔬 Science</TyTag>
              </TyMultiselect>

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                <strong>Categories ({multiselectValues.categories.length}):</strong>{' '}
                {multiselectValues.categories.length > 0 ? multiselectValues.categories.join(', ') : 'None'}
              </div>
            </div>

            {/* Disabled State Demo */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#9ca3af' }}>
                Disabled multiselect:
              </label>
              <TyMultiselect
                placeholder="This multiselect is disabled..."
                disabled
                style={{ width: '100%' }}
              >
                <TyTag value="option1">Option 1</TyTag>
                <TyTag value="option2">Option 2</TyTag>
                <TyTag value="option3">Option 3</TyTag>
              </TyMultiselect>

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#9ca3af' }}>
                Disabled state prevents all interactions
              </div>
            </div>

            {/* Readonly State Demo */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Read-only with pre-selected:
              </label>
              <TyMultiselect
                placeholder="Read-only multiselect..."
                value={['javascript', 'react']}
                readonly
                style={{ width: '100%' }}
              >
                <TyTag value="javascript">JavaScript</TyTag>
                <TyTag value="react">React</TyTag>
                <TyTag value="vue">Vue.js</TyTag>
                <TyTag value="angular">Angular</TyTag>
              </TyMultiselect>

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                Read-only with JavaScript, React pre-selected
              </div>
            </div>
          </div>
        </div>

        {/* Form Integration Tests */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📋 Form Integration & Requirements</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test form integration patterns including labels, required fields, and form submission compatibility.
          </p>

          <form style={{ display: 'grid', gap: '1.5rem' }} onSubmit={(e) => {
            e.preventDefault();
            const formData = new FormData(e.currentTarget);
            console.log('Form submission values:', Object.fromEntries(formData));
            alert('Form submitted! Check console for values.');
          }}>
            {/* Required Field */}
            <div>
              <TyMultiselect
                label="Required Skills"
                placeholder="Select at least one skill (required)..."
                name="required-skills"
                required
                value={multiselectValues.skills}
                flavor="danger"
                onChange={(e) => handleMultiselectChange('skills', e)}
                style={{ width: '100%' }}
              >
                <TyTag value="frontend">Frontend Development</TyTag>
                <TyTag value="backend">Backend Development</TyTag>
                <TyTag value="fullstack">Full Stack Development</TyTag>
                <TyTag value="mobile">Mobile Development</TyTag>
                <TyTag value="devops">DevOps Engineering</TyTag>
                <TyTag value="data">Data Science</TyTag>
                <TyTag value="ml">Machine Learning</TyTag>
                <TyTag value="security">Security Engineering</TyTag>
              </TyMultiselect>
            </div>

            {/* Optional Field with Name */}
            <div>
              <TyMultiselect
                label="Optional Technologies"
                placeholder="Select technologies you've worked with..."
                name="technologies"
                value={multiselectValues.technologies}
                flavor="info"
                onChange={(e) => handleMultiselectChange('technologies', e)}
                style={{ width: '100%' }}
              >
                <TyTag value="react">React</TyTag>
                <TyTag value="vue">Vue.js</TyTag>
                <TyTag value="angular">Angular</TyTag>
                <TyTag value="express">Express.js</TyTag>
                <TyTag value="django">Django</TyTag>
                <TyTag value="rails">Ruby on Rails</TyTag>
                <TyTag value="spring">Spring Boot</TyTag>
                <TyTag value="laravel">Laravel</TyTag>
              </TyMultiselect>
            </div>

            {/* Submit Button */}
            <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
              <TyButton type="button" flavor="neutral" onClick={() => {
                setMultiselectValues({
                  skills: [],
                  technologies: [],
                  colors: [],
                  categories: []
                });
              }}>
                <TyIcon name="x" style={{ marginRight: '8px' }} />
                Clear All
              </TyButton>

              <TyButton type="submit" flavor="primary">
                <TyIcon name="send" style={{ marginRight: '8px' }} />
                Submit Form
              </TyButton>
            </div>
          </form>
        </div>

        {/* Event Log Section */}
        <div style={{
          padding: '1rem',
          background: '#f8fafc',
          border: '1px solid #e2e8f0',
          borderRadius: '8px',
          marginBottom: '1rem'
        }}>
          <h4 style={{ color: '#1f2937', marginTop: 0, marginBottom: '1rem' }}>📋 Multiselect Event Log</h4>
          {multiselectEvents.length > 0 ? (
            <div style={{ maxHeight: '200px', overflow: 'auto' }}>
              {multiselectEvents.map((event, index) => (
                <div key={index} style={{
                  fontSize: '12px',
                  color: '#6b7280',
                  padding: '4px 8px',
                  background: index % 2 === 0 ? 'white' : '#f8fafc',
                  borderRadius: '4px',
                  marginBottom: '2px',
                  fontFamily: 'monospace'
                }}>
                  {event}
                </div>
              ))}
            </div>
          ) : (
            <p style={{ fontSize: '14px', color: '#9ca3af', margin: 0 }}>
              No multiselect events yet. Try selecting and deselecting options to see event details!
            </p>
          )}
        </div>

        {/* Status Display */}
        <div style={{
          padding: '1rem',
          background: '#f0fdf4',
          border: '1px solid #16a34a',
          borderRadius: '8px'
        }}>
          <h4 style={{ color: '#15803d', marginTop: 0 }}>✅ TyMultiselect Implementation Status</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px' }}>
            <div>✅ <strong>TypeScript Interface:</strong> Complete with event types and value handling</div>
            <div>✅ <strong>Event Handling:</strong> change event with detailed action/item data</div>
            <div>✅ <strong>Value Management:</strong> Both string and array value support</div>
            <div>✅ <strong>Form Integration:</strong> Form-associated with name attribute support</div>
            <div>✅ <strong>State Management:</strong> Required/optional fields, validation ready</div>
            <div>✅ <strong>Semantic Flavors:</strong> All flavor variants for different contexts</div>
            <div>✅ <strong>Disabled/Readonly:</strong> Proper state handling for different modes</div>
            <div>✅ <strong>Tag Integration:</strong> Works seamlessly with TyTag children (not TyOption)</div>
            <div>✅ <strong>Tag Management:</strong> Automatic tag creation and dismissal via slots</div>
            <div>✅ <strong>Controlled Components:</strong> Full React-style value control</div>
          </div>
        </div>
      </div>

      {/* CALENDAR TESTING SECTION */}
      <div className="demo-section" style={{
        border: '3px solid #10b981',
        padding: '2rem',
        marginBottom: '2rem',
        borderRadius: '12px',
        background: 'linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%)'
      }}>
        <h2 style={{ color: '#065f46', marginTop: 0 }}>📅 TyCalendar Component Tests</h2>

        <div style={{
          padding: '1.5rem',
          background: '#fef3c7',
          border: '2px solid #f59e0b',
          borderRadius: '8px',
          marginBottom: '2rem'
        }}>
          <h3 style={{ color: '#92400e', marginTop: 0 }}>✨ Calendar Features Demo</h3>
          <p style={{ color: '#92400e', margin: 0 }}>
            Testing the TyCalendar React wrapper with date selection, navigation, custom render functions, form integration, and event handling.
            The calendar orchestrates ty-calendar-navigation and ty-calendar-month components internally.
          </p>
        </div>

        {/* Basic Calendar Functionality */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📆 Basic Calendar Functionality</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test core calendar features including date selection, navigation controls, and event dispatching.
          </p>

          <div style={{ display: 'grid', gap: '2rem', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))' }}>
            {/* Basic Calendar with Navigation */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Basic Calendar:
              </label>
              <TyCalendar
                year={calendarValues.basicCalendar.year}
                month={calendarValues.basicCalendar.month}
                day={calendarValues.basicCalendar.day}
                showNavigation={true}
                onChange={(e) => handleCalendarChange('basicCalendar', e)}
                onNavigate={(e) => handleCalendarNavigate('basicCalendar', e)}
              />

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                <strong>Selected:</strong>{' '}
                {calendarValues.basicCalendar.day
                  ? `${calendarValues.basicCalendar.year}-${calendarValues.basicCalendar.month.toString().padStart(2, '0')}-${calendarValues.basicCalendar.day.toString().padStart(2, '0')}`
                  : 'None'
                }
              </div>
            </div>

            {/* Current Date Calendar */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Current Date Calendar:
              </label>
              <TyCalendar
                year={calendarValues.customCalendar.year}
                month={calendarValues.customCalendar.month}
                day={calendarValues.customCalendar.day}
                showNavigation={true}
                onChange={(e) => handleCalendarChange('customCalendar', e)}
              />

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                <strong>Selected:</strong>{' '}
                {calendarValues.customCalendar.day
                  ? `${calendarValues.customCalendar.year}-${calendarValues.customCalendar.month.toString().padStart(2, '0')}-${calendarValues.customCalendar.day.toString().padStart(2, '0')}`
                  : 'None'
                }
              </div>
            </div>
          </div>
        </div>

        {/* Advanced Calendar Features */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🎨 Advanced Calendar Features</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test advanced features including custom render functions, size controls, and locale support.
          </p>

          <div style={{ display: 'grid', gap: '2rem', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))' }}>
            {/* Custom Day Content */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Custom Day Content:
              </label>
              <TyCalendar
                showNavigation={true}
                dayContentFn={(dayContext: any) => {
                  const day = dayContext['day-in-month'];
                  const isToday = dayContext['today?'];
                  const isWeekend = dayContext['weekend'];

                  // Create a DOM element instead of returning a string
                  const dayElement = document.createElement('div');

                  // Add emojis for special days
                  if (isToday) {
                    dayElement.textContent = `${day} 🌟`;
                    dayElement.style.fontWeight = 'bold';
                    dayElement.style.color = '#dc2626';
                  } else if (isWeekend) {
                    dayElement.textContent = `${day} 🎉`;
                    dayElement.style.color = '#7c3aed';
                  } else if (day === 1) {
                    dayElement.textContent = `${day} 🎯`;
                    dayElement.style.color = '#059669';
                  } else if (day === 15) {
                    dayElement.textContent = `${day} 💎`;
                    dayElement.style.color = '#dc2626';
                    dayElement.style.fontWeight = 'bold';
                  } else {
                    dayElement.textContent = day.toString();
                  }

                  return dayElement;
                }}
              />

              <div style={{ marginTop: '1rem', fontSize: '12px', color: '#6b7280' }}>
                Custom day content with emojis and styling (DOM elements)
              </div>
            </div>

            {/* Sized Calendar */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Sized Calendar:
              </label>
              <TyCalendar
                showNavigation={true}
                width="100%"
                minWidth="280px"
                maxWidth="350px"
              />

              <div style={{ marginTop: '1rem', fontSize: '12px', color: '#6b7280' }}>
                Width: 100%, Min: 280px, Max: 350px
              </div>
            </div>

            {/* Locale Calendar */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Locale Support:
              </label>
              <TyCalendar
                showNavigation={true}
                locale="de-DE"
              />

              <div style={{ marginTop: '1rem', fontSize: '12px', color: '#6b7280' }}>
                German locale (de-DE) for month/day names
              </div>
            </div>

            {/* Current Date Calendar */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Default Calendar:
              </label>
              <TyCalendar
                showNavigation={true}
              />

              <div style={{ marginTop: '1rem', fontSize: '12px', color: '#6b7280' }}>
                Defaults to current month/year with today highlighted
              </div>
            </div>
          </div>
        </div>

        {/* Form Integration */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📋 Form Integration</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test form integration with name attribute and value handling for form submissions.
          </p>

          <form style={{ display: 'grid', gap: '1.5rem' }} onSubmit={(e) => {
            e.preventDefault();
            const formData = new FormData(e.currentTarget);
            console.log('Calendar form values:', Object.fromEntries(formData));
            alert('Form submitted! Check console for values.');
          }}>
            <div style={{ display: 'grid', gap: '1rem', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))' }}>
              <div>
                <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                  Event Date:
                </label>
                <TyCalendar
                  name="event-date"
                  year={calendarValues.formCalendar.year}
                  month={calendarValues.formCalendar.month}
                  day={calendarValues.formCalendar.day}
                  showNavigation={true}
                  onChange={(e) => handleCalendarChange('formCalendar', e)}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                  Due Date:
                </label>
                <TyCalendar
                  name="due-date"
                  value="2025-02-14"  // Valentine's Day
                  showNavigation={true}
                />
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
              <TyButton type="button" flavor="neutral" onClick={() => {
                setCalendarValues(prev => ({
                  ...prev,
                  formCalendar: { year: undefined, month: undefined, day: undefined }
                }));
              }}>
                <TyIcon name="x" style={{ marginRight: '8px' }} />
                Clear
              </TyButton>

              <TyButton type="submit" flavor="primary">
                <TyIcon name="calendar" style={{ marginRight: '8px' }} />
                Submit Dates
              </TyButton>
            </div>
          </form>
        </div>

        {/* Event Log Section */}
        <div style={{
          padding: '1rem',
          background: '#f8fafc',
          border: '1px solid #e2e8f0',
          borderRadius: '8px',
          marginBottom: '1rem'
        }}>
          <h4 style={{ color: '#1f2937', marginTop: 0, marginBottom: '1rem' }}>📋 Calendar Event Log</h4>
          {calendarEvents.length > 0 ? (
            <div style={{ maxHeight: '200px', overflow: 'auto' }}>
              {calendarEvents.map((event, index) => (
                <div key={index} style={{
                  fontSize: '12px',
                  color: '#6b7280',
                  padding: '4px 8px',
                  background: index % 2 === 0 ? 'white' : '#f8fafc',
                  borderRadius: '4px',
                  marginBottom: '2px',
                  fontFamily: 'monospace'
                }}>
                  {event}
                </div>
              ))}
            </div>
          ) : (
            <p style={{ fontSize: '14px', color: '#9ca3af', margin: 0 }}>
              No calendar events yet. Try selecting dates or navigating between months to see event details!
            </p>
          )}
        </div>

        {/* Status Display */}
        <div style={{
          padding: '1rem',
          background: '#f0fdf4',
          border: '1px solid #16a34a',
          borderRadius: '8px'
        }}>
          <h4 style={{ color: '#15803d', marginTop: 0 }}>✅ TyCalendar Implementation Status</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px' }}>
            <div>✅ <strong>TypeScript Interface:</strong> Complete with change and navigate event types</div>
            <div>✅ <strong>Dual Event System:</strong> onChange for date selection, onNavigate for month/year changes</div>
            <div>✅ <strong>Attribute Mapping:</strong> year/month/day attributes with proper number conversion</div>
            <div>✅ <strong>Property Functions:</strong> dayContentFn and dayClassesFn as React functions</div>
            <div>✅ <strong>Form Integration:</strong> Form-associated with name and value attributes</div>
            <div>✅ <strong>Navigation Control:</strong> showNavigation boolean for hiding/showing navigation</div>
            <div>✅ <strong>Locale Support:</strong> Internationalization with locale attribute</div>
            <div>✅ <strong>Size Control:</strong> width, minWidth, maxWidth for responsive sizing</div>
            <div>✅ <strong>Custom Rendering:</strong> dayContentFn for custom day content, customCSS injection</div>
            <div>✅ <strong>Component Architecture:</strong> Orchestrates ty-calendar-navigation and ty-calendar-month</div>
          </div>
        </div>
      </div>

      {/* DATEPICKER TESTING SECTION */}
      <div className="demo-section" style={{
        border: '3px solid #3b82f6',
        padding: '2rem',
        marginBottom: '2rem',
        borderRadius: '12px',
        background: 'linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%)'
      }}>
        <h2 style={{ color: '#1d4ed8', marginTop: 0 }}>📅 TyDatePicker Component Tests</h2>

        <div style={{
          padding: '1.5rem',
          background: '#fef3c7',
          border: '2px solid #f59e0b',
          borderRadius: '8px',
          marginBottom: '2rem'
        }}>
          <h3 style={{ color: '#92400e', marginTop: 0 }}>✨ DatePicker Features Demo</h3>
          <p style={{ color: '#92400e', margin: 0 }}>
            Testing the TyDatePicker React wrapper with date selection, input styling, time support, form integration, and event handling.
            The date picker provides a read-only input with calendar dropdown for date selection.
          </p>
        </div>

        {/* Basic DatePicker Functionality */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📆 Basic DatePicker Functionality</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test core date picker features including date selection, input display, clearable option, and event handling.
          </p>

          <div style={{ display: 'grid', gap: '2rem', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))' }}>
            {/* Basic DatePicker */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Basic DatePicker:
              </label>
              <TyDatePicker
                placeholder="Select a date..."
                value={datePickerValues.basicDatePicker}
                clearable
                onChange={(e) => handleDatePickerChange('basicDatePicker', e)}
                onOpen={(e) => handleDatePickerOpen('basicDatePicker', e)}
                onClose={(e) => handleDatePickerClose('basicDatePicker', e)}
              />

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                <strong>Selected:</strong> {datePickerValues.basicDatePicker || 'None'}
              </div>
            </div>

            {/* DatePicker with Time */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                DatePicker with Time:
              </label>
              <TyDatePicker
                placeholder="Select date and time..."
                value={datePickerValues.timePickerExample}
                withTime
                clearable
                flavor="primary"
                onChange={(e) => handleDatePickerChange('timePickerExample', e)}
              />

              <div style={{ marginTop: '1rem', fontSize: '13px', color: '#6b7280' }}>
                <strong>Selected:</strong> {datePickerValues.timePickerExample || 'None'}
              </div>
            </div>
          </div>
        </div>

        {/* Advanced DatePicker Features */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🎨 Advanced DatePicker Features</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test advanced features including size variants, flavors, date formatting, locale support, and date constraints.
          </p>

          <div style={{ display: 'grid', gap: '2rem', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))' }}>
            {/* Size Variants */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Size Variants:
              </label>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                <TyDatePicker
                  placeholder="Small size..."
                  size="sm"
                  flavor="success"
                />
                <TyDatePicker
                  placeholder="Medium size (default)..."
                  size="md"
                  flavor="primary"
                />
                <TyDatePicker
                  placeholder="Large size..."
                  size="lg"
                  flavor="warning"
                />
              </div>
            </div>

            {/* Format Options */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Format Options:
              </label>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                <TyDatePicker
                  placeholder="Short format..."
                  format="short"
                  value="2025-01-15"
                  flavor="info"
                />
                <TyDatePicker
                  placeholder="Medium format..."
                  format="medium"
                  value="2025-01-15"
                  flavor="secondary"
                />
                <TyDatePicker
                  placeholder="Long format..."
                  format="long"
                  value="2025-01-15"
                  flavor="success"
                />
              </div>
            </div>

            {/* Locale Support */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Locale Support:
              </label>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                <TyDatePicker
                  placeholder="English (US)..."
                  locale="en-US"
                  value="2025-01-15"
                  format="long"
                />
                <TyDatePicker
                  placeholder="German..."
                  locale="de-DE"
                  value="2025-01-15"
                  format="long"
                />
                <TyDatePicker
                  placeholder="French..."
                  locale="fr-FR"
                  value="2025-01-15"
                  format="long"
                />
              </div>
            </div>

            {/* Date Constraints */}
            <div>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', marginBottom: '0.5rem', color: '#374151' }}>
                Date Constraints:
              </label>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                <TyDatePicker
                  placeholder="Min date (today)..."
                  minDate={new Date().toISOString().split('T')[0]}
                  flavor="warning"
                />
                <TyDatePicker
                  placeholder="Max date (end of month)..."
                  maxDate="2025-01-31"
                  flavor="danger"
                />
                <TyDatePicker
                  placeholder="Date range (this month)..."
                  minDate="2025-01-01"
                  maxDate="2025-01-31"
                  flavor="info"
                />
              </div>
            </div>
          </div>
        </div>

        {/* Form Integration */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📋 Form Integration</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test form integration with labels, required fields, disabled states, and form submission compatibility.
          </p>

          <form style={{ display: 'grid', gap: '1.5rem' }} onSubmit={(e) => {
            e.preventDefault();
            const formData = new FormData(e.currentTarget);
            console.log('DatePicker form values:', Object.fromEntries(formData));
            alert('Form submitted! Check console for values.');
          }}>
            <div style={{ display: 'grid', gap: '1rem', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))' }}>
              <div>
                <TyDatePicker
                  label="Event Date (Required)"
                  placeholder="Select event date..."
                  name="event-date"
                  required
                  flavor="danger"
                  value={datePickerValues.formDatePicker}
                  onChange={(e) => handleDatePickerChange('formDatePicker', e)}
                />
              </div>

              <div>
                <TyDatePicker
                  label="Meeting Time (Optional)"
                  placeholder="Select meeting time..."
                  name="meeting-time"
                  withTime
                  flavor="primary"
                  clearable
                />
              </div>

              <div>
                <TyDatePicker
                  label="Deadline (Disabled)"
                  placeholder="Cannot select..."
                  name="deadline"
                  value="2025-02-14"
                  disabled
                  flavor="neutral"
                />
              </div>

              <div>
                <TyDatePicker
                  label="Birth Date"
                  placeholder="Select birth date..."
                  name="birth-date"
                  maxDate="2010-01-01"
                  format="medium"
                  flavor="info"
                />
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
              <TyButton type="button" flavor="neutral" onClick={() => {
                setDatePickerValues({
                  basicDatePicker: '',
                  formDatePicker: '',
                  timePickerExample: '',
                  rangeExample: { start: '', end: '' }
                });
              }}>
                <TyIcon name="x" style={{ marginRight: '8px' }} />
                Clear All
              </TyButton>

              <TyButton type="submit" flavor="primary">
                <TyIcon name="calendar" style={{ marginRight: '8px' }} />
                Submit Dates
              </TyButton>
            </div>
          </form>
        </div>

        {/* Event Log Section */}
        <div style={{
          padding: '1rem',
          background: '#f8fafc',
          border: '1px solid #e2e8f0',
          borderRadius: '8px',
          marginBottom: '1rem'
        }}>
          <h4 style={{ color: '#1f2937', marginTop: 0, marginBottom: '1rem' }}>📋 DatePicker Event Log</h4>
          {datePickerEvents.length > 0 ? (
            <div style={{ maxHeight: '200px', overflow: 'auto' }}>
              {datePickerEvents.map((event, index) => (
                <div key={index} style={{
                  fontSize: '12px',
                  color: '#6b7280',
                  padding: '4px 8px',
                  background: index % 2 === 0 ? 'white' : '#f8fafc',
                  borderRadius: '4px',
                  marginBottom: '2px',
                  fontFamily: 'monospace'
                }}>
                  {event}
                </div>
              ))}
            </div>
          ) : (
            <p style={{ fontSize: '14px', color: '#9ca3af', margin: 0 }}>
              No date picker events yet. Try selecting dates to see event details!
            </p>
          )}
        </div>

        {/* Status Display */}
        <div style={{
          padding: '1rem',
          background: '#eff6ff',
          border: '1px solid #3b82f6',
          borderRadius: '8px'
        }}>
          <h4 style={{ color: '#1d4ed8', marginTop: 0 }}>✅ TyDatePicker Implementation Status</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px' }}>
            <div>✅ <strong>TypeScript Interface:</strong> Complete with change, open, and close event types</div>
            <div>✅ <strong>Event Handling:</strong> change, open, close events with detailed data</div>
            <div>✅ <strong>Value Management:</strong> ISO string and formatted value support</div>
            <div>✅ <strong>Form Integration:</strong> Form-associated with name attribute support</div>
            <div>✅ <strong>Time Support:</strong> Optional time picker with withTime prop</div>
            <div>✅ <strong>Date Constraints:</strong> minDate and maxDate for validation</div>
            <div>✅ <strong>Internationalization:</strong> Full locale support for formatting</div>
            <div>✅ <strong>Size & Flavors:</strong> Multiple size variants and semantic flavors</div>
            <div>✅ <strong>Accessibility:</strong> Proper labels, required states, disabled handling</div>
            <div>✅ <strong>Input Features:</strong> Clearable option, placeholder text, read-only input</div>
          </div>
        </div>
      </div>

      {/* POPUP TESTING SECTION */}
      <div className="demo-section" style={{
        border: '3px solid #7c3aed',
        padding: '2rem',
        marginBottom: '2rem',
        borderRadius: '12px',
        background: 'linear-gradient(135deg, #faf5ff 0%, #f3e8ff 100%)'
      }}>
        <h2 style={{ color: '#6b21a8', marginTop: 0 }}>🎯 TyPopup Component Tests</h2>

        <div style={{
          padding: '1.5rem',
          background: '#fef3c7',
          border: '2px solid #f59e0b',
          borderRadius: '8px',
          marginBottom: '2rem'
        }}>
          <h3 style={{ color: '#92400e', marginTop: 0 }}>✨ Popup Features Demo</h3>
          <p style={{ color: '#92400e', margin: 0 }}>
            Testing the TyPopup React wrapper with anchor-based positioning, placement options, auto-positioning, and slot-based content projection.
            The popup provides a powerful positioning primitive for floating elements like tooltips, dropdowns, and context menus.
          </p>
        </div>

        {/* Basic Popup Functionality */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🎯 Basic Popup Functionality</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test core popup features including anchor-based positioning, open/close states, and slot-based content.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '2rem', alignItems: 'center', minHeight: '120px' }}>
            {/* Basic Popup */}
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.basicPopup}>
                <TyButton 
                  flavor="primary" 
                  onClick={() => togglePopup('basicPopup')}
                  slot="anchor"
                >
                  <TyIcon name="info" style={{ marginRight: '8px' }} />
                  Basic Popup
                </TyButton>
                
                <div style={{
                  background: 'white',
                  border: '1px solid #e5e7eb',
                  borderRadius: '8px',
                  padding: '1rem',
                  boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1)',
                  maxWidth: '250px'
                }}>
                  <h4 style={{ margin: '0 0 0.5rem 0', fontSize: '14px', fontWeight: '600' }}>Basic Popup Content</h4>
                  <p style={{ margin: 0, fontSize: '13px', color: '#6b7280' }}>
                    This is a basic popup with auto-positioning relative to the anchor button.
                  </p>
                </div>
              </TyPopup>
            </div>

            {/* Tooltip-style Popup */}
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.tooltipPopup} placement="top" offset={12}>
                <TyButton 
                  flavor="success" 
                  onClick={() => togglePopup('tooltipPopup')}
                  slot="anchor"
                >
                  <TyIcon name="help-circle" style={{ marginRight: '8px' }} />
                  Tooltip Style
                </TyButton>
                
                <div style={{
                  background: '#1f2937',
                  color: 'white',
                  borderRadius: '6px',
                  padding: '8px 12px',
                  fontSize: '13px',
                  boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
                  maxWidth: '200px'
                }}>
                  Tooltip-style popup positioned above the anchor
                </div>
              </TyPopup>
            </div>

            {/* Context Menu Popup */}
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.contextMenuPopup} placement="right" offset={8}>
                <TyButton 
                  flavor="warning" 
                  onClick={() => togglePopup('contextMenuPopup')}
                  slot="anchor"
                >
                  <TyIcon name="menu" style={{ marginRight: '8px' }} />
                  Context Menu
                </TyButton>
                
                <div style={{
                  background: 'white',
                  border: '1px solid #e5e7eb',
                  borderRadius: '8px',
                  padding: '0.5rem 0',
                  boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1)',
                  minWidth: '150px'
                }}>
                  {['Edit', 'Copy', 'Delete', 'Share'].map((action, index) => (
                    <div key={action} style={{
                      padding: '0.5rem 1rem',
                      fontSize: '14px',
                      cursor: 'pointer',
                      borderBottom: index < 3 ? '1px solid #f3f4f6' : 'none'
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.background = '#f9fafb'}
                    onMouseLeave={(e) => e.currentTarget.style.background = 'transparent'}
                    onClick={() => {
                      console.log(`${action} clicked`);
                      togglePopup('contextMenuPopup');
                    }}>
                      {action}
                    </div>
                  ))}
                </div>
              </TyPopup>
            </div>

            {/* Global Close Button */}
            <TyButton flavor="neutral" onClick={closeAllPopups}>
              <TyIcon name="x" style={{ marginRight: '8px' }} />
              Close All
            </TyButton>
          </div>
        </div>

        {/* Placement Testing */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>📍 Placement & Positioning</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test different popup placements and auto-flipping behavior based on available viewport space.
          </p>

          <div style={{ 
            display: 'grid', 
            gap: '3rem', 
            gridTemplateColumns: 'repeat(3, 1fr)',
            justifyItems: 'center',
            alignItems: 'center',
            minHeight: '200px',
            padding: '2rem'
          }}>
            {/* Top Placement */}
            <div></div>
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.positionTests.top} placement="top" offset={16}>
                <TyButton 
                  flavor="info" 
                  onClick={() => togglePopup('positionTests.top')}
                  slot="anchor"
                >
                  Top Placement
                </TyButton>
                
                <div style={{
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  color: 'white',
                  borderRadius: '8px',
                  padding: '1rem',
                  textAlign: 'center',
                  minWidth: '150px'
                }}>
                  <div style={{ fontWeight: '600', marginBottom: '4px' }}>Top Popup</div>
                  <div style={{ fontSize: '12px', opacity: 0.9 }}>Positioned above anchor</div>
                </div>
              </TyPopup>
            </div>
            <div></div>

            {/* Left Placement */}
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.positionTests.left} placement="left" offset={16}>
                <TyButton 
                  flavor="secondary" 
                  onClick={() => togglePopup('positionTests.left')}
                  slot="anchor"
                >
                  Left Placement
                </TyButton>
                
                <div style={{
                  background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
                  color: 'white',
                  borderRadius: '8px',
                  padding: '1rem',
                  textAlign: 'center',
                  minWidth: '120px'
                }}>
                  <div style={{ fontWeight: '600', marginBottom: '4px' }}>Left</div>
                  <div style={{ fontSize: '12px', opacity: 0.9 }}>←</div>
                </div>
              </TyPopup>
            </div>

            {/* Center Reference */}
            <div style={{
              background: '#f3f4f6',
              border: '2px dashed #9ca3af',
              borderRadius: '8px',
              padding: '1rem',
              textAlign: 'center',
              fontSize: '14px',
              color: '#6b7280'
            }}>
              <div style={{ fontWeight: '600', marginBottom: '4px' }}>Center Reference</div>
              <div style={{ fontSize: '12px' }}>All popups position relative to their anchors</div>
            </div>

            {/* Right Placement */}
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.positionTests.right} placement="right" offset={16}>
                <TyButton 
                  flavor="success" 
                  onClick={() => togglePopup('positionTests.right')}
                  slot="anchor"
                >
                  Right Placement
                </TyButton>
                
                <div style={{
                  background: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
                  color: 'white',
                  borderRadius: '8px',
                  padding: '1rem',
                  textAlign: 'center',
                  minWidth: '120px'
                }}>
                  <div style={{ fontWeight: '600', marginBottom: '4px' }}>Right</div>
                  <div style={{ fontSize: '12px', opacity: 0.9 }}>→</div>
                </div>
              </TyPopup>
            </div>

            {/* Bottom Placement */}
            <div></div>
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.positionTests.bottom} placement="bottom" offset={16}>
                <TyButton 
                  flavor="danger" 
                  onClick={() => togglePopup('positionTests.bottom')}
                  slot="anchor"
                >
                  Bottom Placement
                </TyButton>
                
                <div style={{
                  background: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
                  color: 'white',
                  borderRadius: '8px',
                  padding: '1rem',
                  textAlign: 'center',
                  minWidth: '150px'
                }}>
                  <div style={{ fontWeight: '600', marginBottom: '4px' }}>Bottom Popup</div>
                  <div style={{ fontSize: '12px', opacity: 0.9 }}>Positioned below anchor</div>
                </div>
              </TyPopup>
            </div>
            <div></div>
          </div>
        </div>

        {/* Advanced Features */}
        <div style={{
          marginBottom: '2rem',
          padding: '1.5rem',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
        }}>
          <h3 style={{ marginTop: 0, color: '#1f2937' }}>🔧 Advanced Features</h3>
          <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1.5rem' }}>
            Test advanced popup features including custom offsets, flip behavior, and complex content types.
          </p>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '2rem', alignItems: 'center' }}>
            {/* Custom Offset */}
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.dropdownPopup} placement="bottom" offset={2} flip>
                <TyButton 
                  flavor="primary" 
                  onClick={() => togglePopup('dropdownPopup')}
                  slot="anchor"
                >
                  <TyIcon name="chevron-down" style={{ marginRight: '8px' }} />
                  Custom Dropdown
                </TyButton>
                
                <div style={{
                  background: 'white',
                  border: '1px solid #d1d5db',
                  borderRadius: '8px',
                  padding: '0.5rem',
                  boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1)',
                  minWidth: '200px'
                }}>
                  <div style={{ padding: '0.5rem', fontWeight: '600', fontSize: '14px', borderBottom: '1px solid #f3f4f6' }}>
                    Options
                  </div>
                  {['Option 1', 'Option 2', 'Option 3', 'Option 4'].map((option, index) => (
                    <div key={option} style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: '8px',
                      padding: '0.5rem',
                      fontSize: '14px',
                      cursor: 'pointer',
                      borderRadius: '4px'
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.background = '#f9fafb'}
                    onMouseLeave={(e) => e.currentTarget.style.background = 'transparent'}
                    onClick={() => {
                      console.log(`${option} selected`);
                      togglePopup('dropdownPopup');
                    }}>
                      <TyIcon name="check" style={{ fontSize: '12px' }} />
                      {option}
                    </div>
                  ))}
                </div>
              </TyPopup>
            </div>

            {/* Complex Content */}
            <div style={{ position: 'relative' }}>
              <TyPopup open={popupStates.tooltipPopup} placement="top" offset={12}>
                <div 
                  style={{
                    display: 'inline-flex',
                    alignItems: 'center',
                    gap: '8px',
                    padding: '8px 12px',
                    background: '#f3f4f6',
                    borderRadius: '6px',
                    cursor: 'pointer',
                    border: '1px solid #e5e7eb'
                  }}
                  onClick={() => togglePopup('tooltipPopup')}
                  slot="anchor"
                >
                  <TyIcon name="star" style={{ color: '#f59e0b' }} />
                  <span style={{ fontSize: '14px', fontWeight: '500' }}>Custom Anchor</span>
                </div>
                
                <div style={{
                  background: 'white',
                  border: '1px solid #e5e7eb',
                  borderRadius: '12px',
                  padding: '1.5rem',
                  boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)',
                  maxWidth: '300px'
                }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '1rem' }}>
                    <div style={{
                      width: '40px',
                      height: '40px',
                      borderRadius: '50%',
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      color: 'white',
                      fontWeight: '600'
                    }}>
                      JS
                    </div>
                    <div>
                      <div style={{ fontWeight: '600', fontSize: '16px' }}>John Smith</div>
                      <div style={{ fontSize: '14px', color: '#6b7280' }}>Senior Developer</div>
                    </div>
                  </div>
                  <p style={{ margin: '0', fontSize: '14px', color: '#374151' }}>
                    Complex popup content with profile information, images, and interactive elements.
                  </p>
                  <div style={{ marginTop: '1rem', display: 'flex', gap: '0.5rem' }}>
                    <TyButton size="sm" flavor="primary">View Profile</TyButton>
                    <TyButton size="sm" flavor="neutral">Message</TyButton>
                  </div>
                </div>
              </TyPopup>
            </div>
          </div>
        </div>

        {/* Usage Guidelines */}
        <div style={{
          marginBottom: '1rem',
          padding: '1rem',
          background: '#f0f9ff',
          border: '1px solid #0ea5e9',
          borderRadius: '8px'
        }}>
          <h4 style={{ color: '#0c4a6e', marginTop: 0, marginBottom: '1rem' }}>💡 Usage Guidelines</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px', color: '#0c4a6e' }}>
            <div><strong>Anchor Slot:</strong> Use <code>slot="anchor"</code> on the element that triggers the popup</div>
            <div><strong>Content Slot:</strong> Default slot contains the popup content (no slot attribute needed)</div>
            <div><strong>Positioning:</strong> The popup automatically calculates optimal position based on viewport space</div>
            <div><strong>Auto-Update:</strong> Position updates automatically on scroll, resize, or anchor changes</div>
            <div><strong>Performance:</strong> Uses ResizeObserver and requestAnimationFrame for efficient updates</div>
          </div>
        </div>

        {/* Status Display */}
        <div style={{
          padding: '1rem',
          background: '#faf5ff',
          border: '1px solid #7c3aed',
          borderRadius: '8px'
        }}>
          <h4 style={{ color: '#6b21a8', marginTop: 0 }}>✅ TyPopup Implementation Status</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px' }}>
            <div>✅ <strong>TypeScript Interface:</strong> Complete with all placement and offset options</div>
            <div>✅ <strong>Slot-Based Content:</strong> Anchor and content slots with proper React integration</div>
            <div>✅ <strong>Positioning Engine:</strong> Smart placement with viewport-aware auto-flipping</div>
            <div>✅ <strong>Auto-Update System:</strong> ResizeObserver and scroll listeners for dynamic positioning</div>
            <div>✅ <strong>Performance Optimized:</strong> Debounced updates and requestAnimationFrame</div>
            <div>✅ <strong>Flexible Anchors:</strong> Works with any element as anchor via slot system</div>
            <div>✅ <strong>Rich Content Support:</strong> Complex layouts, interactive elements, forms</div>
            <div>✅ <strong>Boolean Props:</strong> Correct handling of open and flip attributes</div>
            <div>✅ <strong>Offset Control:</strong> Customizable distance from anchor element</div>
            <div>✅ <strong>Framework Agnostic:</strong> Works with React while remaining web standard</div>
          </div>
        </div>
      </div>

      {/* SCROLL PREVENTION TEST SECTION */}
      <div className="demo-section" style={{
        border: '3px solid #2563eb',
        padding: '2rem',
        marginBottom: '2rem',
        borderRadius: '12px',
        background: 'linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%)'
      }}>
        <h2 style={{ color: '#1d4ed8', marginTop: 0 }}>📜 Scroll Prevention Test</h2>

        <div style={{
          padding: '1.5rem',
          background: '#fef3c7',
          border: '2px solid #f59e0b',
          borderRadius: '8px',
          marginBottom: '2rem'
        }}>
          <h3 style={{ color: '#92400e', marginTop: 0 }}>🔧 Testing Instructions</h3>
          <ol style={{ color: '#92400e', margin: 0, paddingLeft: '1.5rem' }}>
            <li><strong>📏 Notice:</strong> This page has a lot of content (scroll down to see more)</li>
            <li><strong>🖱️ Open dropdown:</strong> Click on the "Countries" dropdown below</li>
            <li><strong>✅ Verify:</strong> Body/page scrolling is locked (try scrolling the page with mouse wheel)</li>
            <li><strong>🔄 Verify:</strong> Internal dropdown scrolling works (scroll within dropdown options)</li>
            <li><strong>❌ Close dropdown:</strong> Click outside or press Escape</li>
            <li><strong>✅ Verify:</strong> Page scrolling is restored</li>
            <li><strong>🎯 Try:</strong> Keyboard navigation (arrows) and search filtering</li>
            <li><strong>🔀 Test:</strong> Open both dropdowns simultaneously</li>
          </ol>
        </div>

        <div style={{
          display: 'grid',
          gap: '2rem',
          gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))',
          marginBottom: '2rem'
        }}>
          {/* Large Countries Dropdown */}
          <div style={{
            padding: '1.5rem',
            background: 'white',
            borderRadius: '8px',
            boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
          }}>
            <h3 style={{ marginTop: 0, color: '#1f2937' }}>🌍 Countries (100+ Options)</h3>
            <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
              <strong>Body scroll should be locked</strong> when this dropdown is open.<br />
              <strong>Internal scroll should work</strong> inside the dropdown options.
            </p>

            <TyDropdown
              placeholder="Search countries (try typing 'united')..."
              label="Select Country"
              value={dropdownValues.countries}
              searchable
              style={{ minWidth: '280px' }}
              onChange={(e) => handleDropdownChange('countries', e)}
            >
              {/* 100+ countries to force scrollable dropdown */}
              <TyOption value="af">🇦🇫 Afghanistan</TyOption>
              <TyOption value="al">🇦🇱 Albania</TyOption>
              <TyOption value="dz">🇩🇿 Algeria</TyOption>
              <TyOption value="ad">🇦🇩 Andorra</TyOption>
              <TyOption value="ao">🇦🇴 Angola</TyOption>
              <TyOption value="ag">🇦🇬 Antigua and Barbuda</TyOption>
              <TyOption value="ar">🇦🇷 Argentina</TyOption>
              <TyOption value="am">🇦🇲 Armenia</TyOption>
              <TyOption value="au">🇦🇺 Australia</TyOption>
              <TyOption value="at">🇦🇹 Austria</TyOption>
              <TyOption value="az">🇦🇿 Azerbaijan</TyOption>
              <TyOption value="bs">🇧🇸 Bahamas</TyOption>
              <TyOption value="bh">🇧🇭 Bahrain</TyOption>
              <TyOption value="bd">🇧🇩 Bangladesh</TyOption>
              <TyOption value="bb">🇧🇧 Barbados</TyOption>
              <TyOption value="by">🇧🇾 Belarus</TyOption>
              <TyOption value="be">🇧🇪 Belgium</TyOption>
              <TyOption value="bz">🇧🇿 Belize</TyOption>
              <TyOption value="bj">🇧🇯 Benin</TyOption>
              <TyOption value="bt">🇧🇹 Bhutan</TyOption>
              <TyOption value="bo">🇧🇴 Bolivia</TyOption>
              <TyOption value="ba">🇧🇦 Bosnia and Herzegovina</TyOption>
              <TyOption value="bw">🇧🇼 Botswana</TyOption>
              <TyOption value="br">🇧🇷 Brazil</TyOption>
              <TyOption value="bn">🇧🇳 Brunei</TyOption>
              <TyOption value="bg">🇧🇬 Bulgaria</TyOption>
              <TyOption value="bf">🇧🇫 Burkina Faso</TyOption>
              <TyOption value="bi">🇧🇮 Burundi</TyOption>
              <TyOption value="kh">🇰🇭 Cambodia</TyOption>
              <TyOption value="cm">🇨🇲 Cameroon</TyOption>
              <TyOption value="ca">🇨🇦 Canada</TyOption>
              <TyOption value="cv">🇨🇻 Cape Verde</TyOption>
              <TyOption value="cf">🇨🇫 Central African Republic</TyOption>
              <TyOption value="td">🇹🇩 Chad</TyOption>
              <TyOption value="cl">🇨🇱 Chile</TyOption>
              <TyOption value="cn">🇨🇳 China</TyOption>
              <TyOption value="co">🇨🇴 Colombia</TyOption>
              <TyOption value="km">🇰🇲 Comoros</TyOption>
              <TyOption value="cg">🇨🇬 Congo</TyOption>
              <TyOption value="cd">🇨🇩 Congo (Democratic Republic)</TyOption>
              <TyOption value="cr">🇨🇷 Costa Rica</TyOption>
              <TyOption value="hr">🇭🇷 Croatia</TyOption>
              <TyOption value="cu">🇨🇺 Cuba</TyOption>
              <TyOption value="cy">🇨🇾 Cyprus</TyOption>
              <TyOption value="cz">🇨🇿 Czech Republic</TyOption>
              <TyOption value="dk">🇩🇰 Denmark</TyOption>
              <TyOption value="dj">🇩🇯 Djibouti</TyOption>
              <TyOption value="dm">🇩🇲 Dominica</TyOption>
              <TyOption value="do">🇩🇴 Dominican Republic</TyOption>
              <TyOption value="ec">🇪🇨 Ecuador</TyOption>
              <TyOption value="eg">🇪🇬 Egypt</TyOption>
              <TyOption value="sv">🇸🇻 El Salvador</TyOption>
              <TyOption value="gq">🇬🇶 Equatorial Guinea</TyOption>
              <TyOption value="er">🇪🇷 Eritrea</TyOption>
              <TyOption value="ee">🇪🇪 Estonia</TyOption>
              <TyOption value="et">🇪🇹 Ethiopia</TyOption>
              <TyOption value="fj">🇫🇯 Fiji</TyOption>
              <TyOption value="fi">🇫🇮 Finland</TyOption>
              <TyOption value="fr">🇫🇷 France</TyOption>
              <TyOption value="ga">🇬🇦 Gabon</TyOption>
              <TyOption value="gm">🇬🇲 Gambia</TyOption>
              <TyOption value="ge">🇬🇪 Georgia</TyOption>
              <TyOption value="de">🇩🇪 Germany</TyOption>
              <TyOption value="gh">🇬🇭 Ghana</TyOption>
              <TyOption value="gr">🇬🇷 Greece</TyOption>
              <TyOption value="gd">🇬🇩 Grenada</TyOption>
              <TyOption value="gt">🇬🇹 Guatemala</TyOption>
              <TyOption value="gn">🇬🇳 Guinea</TyOption>
              <TyOption value="gw">🇬🇼 Guinea-Bissau</TyOption>
              <TyOption value="gy">🇬🇾 Guyana</TyOption>
              <TyOption value="ht">🇭🇹 Haiti</TyOption>
              <TyOption value="hn">🇭🇳 Honduras</TyOption>
              <TyOption value="hu">🇭🇺 Hungary</TyOption>
              <TyOption value="is">🇮🇸 Iceland</TyOption>
              <TyOption value="in">🇮🇳 India</TyOption>
              <TyOption value="id">🇮🇩 Indonesia</TyOption>
              <TyOption value="ir">🇮🇷 Iran</TyOption>
              <TyOption value="iq">🇮🇶 Iraq</TyOption>
              <TyOption value="ie">🇮🇪 Ireland</TyOption>
              <TyOption value="il">🇮🇱 Israel</TyOption>
              <TyOption value="it">🇮🇹 Italy</TyOption>
              <TyOption value="jm">🇯🇲 Jamaica</TyOption>
              <TyOption value="jp">🇯🇵 Japan</TyOption>
              <TyOption value="jo">🇯🇴 Jordan</TyOption>
              <TyOption value="kz">🇰🇿 Kazakhstan</TyOption>
              <TyOption value="ke">🇰🇪 Kenya</TyOption>
              <TyOption value="ki">🇰🇮 Kiribati</TyOption>
              <TyOption value="kp">🇰🇵 North Korea</TyOption>
              <TyOption value="kr">🇰🇷 South Korea</TyOption>
              <TyOption value="kw">🇰🇼 Kuwait</TyOption>
              <TyOption value="kg">🇰🇬 Kyrgyzstan</TyOption>
              <TyOption value="la">🇱🇦 Laos</TyOption>
              <TyOption value="lv">🇱🇻 Latvia</TyOption>
              <TyOption value="lb">🇱🇧 Lebanon</TyOption>
              <TyOption value="ls">🇱🇸 Lesotho</TyOption>
              <TyOption value="lr">🇱🇷 Liberia</TyOption>
              <TyOption value="ly">🇱🇾 Libya</TyOption>
              <TyOption value="li">🇱🇮 Liechtenstein</TyOption>
              <TyOption value="lt">🇱🇹 Lithuania</TyOption>
              <TyOption value="lu">🇱🇺 Luxembourg</TyOption>
              <TyOption value="mk">🇲🇰 North Macedonia</TyOption>
              <TyOption value="mg">🇲🇬 Madagascar</TyOption>
              <TyOption value="mw">🇲🇼 Malawi</TyOption>
              <TyOption value="my">🇲🇾 Malaysia</TyOption>
              <TyOption value="mv">🇲🇻 Maldives</TyOption>
              <TyOption value="ml">🇲🇱 Mali</TyOption>
              <TyOption value="mt">🇲🇹 Malta</TyOption>
              <TyOption value="mh">🇲🇭 Marshall Islands</TyOption>
              <TyOption value="mr">🇲🇷 Mauritania</TyOption>
              <TyOption value="mu">🇲🇺 Mauritius</TyOption>
              <TyOption value="mx">🇲🇽 Mexico</TyOption>
              <TyOption value="fm">🇫🇲 Micronesia</TyOption>
              <TyOption value="md">🇲🇩 Moldova</TyOption>
              <TyOption value="mc">🇲🇨 Monaco</TyOption>
              <TyOption value="mn">🇲🇳 Mongolia</TyOption>
              <TyOption value="me">🇲🇪 Montenegro</TyOption>
              <TyOption value="ma">🇲🇦 Morocco</TyOption>
              <TyOption value="mz">🇲🇿 Mozambique</TyOption>
              <TyOption value="mm">🇲🇲 Myanmar</TyOption>
              <TyOption value="na">🇳🇦 Namibia</TyOption>
              <TyOption value="nr">🇳🇷 Nauru</TyOption>
              <TyOption value="np">🇳🇵 Nepal</TyOption>
              <TyOption value="nl">🇳🇱 Netherlands</TyOption>
              <TyOption value="nz">🇳🇿 New Zealand</TyOption>
              <TyOption value="ni">🇳🇮 Nicaragua</TyOption>
              <TyOption value="ne">🇳🇪 Niger</TyOption>
              <TyOption value="ng">🇳🇬 Nigeria</TyOption>
              <TyOption value="no">🇳🇴 Norway</TyOption>
              <TyOption value="om">🇴🇲 Oman</TyOption>
              <TyOption value="pk">🇵🇰 Pakistan</TyOption>
              <TyOption value="pw">🇵🇼 Palau</TyOption>
              <TyOption value="pa">🇵🇦 Panama</TyOption>
              <TyOption value="pg">🇵🇬 Papua New Guinea</TyOption>
              <TyOption value="py">🇵🇾 Paraguay</TyOption>
              <TyOption value="pe">🇵🇪 Peru</TyOption>
              <TyOption value="ph">🇵🇭 Philippines</TyOption>
              <TyOption value="pl">🇵🇱 Poland</TyOption>
              <TyOption value="pt">🇵🇹 Portugal</TyOption>
              <TyOption value="qa">🇶🇦 Qatar</TyOption>
              <TyOption value="ro">🇷🇴 Romania</TyOption>
              <TyOption value="ru">🇷🇺 Russia</TyOption>
              <TyOption value="rw">🇷🇼 Rwanda</TyOption>
              <TyOption value="kn">🇰🇳 Saint Kitts and Nevis</TyOption>
              <TyOption value="lc">🇱🇨 Saint Lucia</TyOption>
              <TyOption value="vc">🇻🇨 Saint Vincent and the Grenadines</TyOption>
              <TyOption value="ws">🇼🇸 Samoa</TyOption>
              <TyOption value="sm">🇸🇲 San Marino</TyOption>
              <TyOption value="st">🇸🇹 São Tomé and Príncipe</TyOption>
              <TyOption value="sa">🇸🇦 Saudi Arabia</TyOption>
              <TyOption value="sn">🇸🇳 Senegal</TyOption>
              <TyOption value="rs">🇷🇸 Serbia</TyOption>
              <TyOption value="sc">🇸🇨 Seychelles</TyOption>
              <TyOption value="sl">🇸🇱 Sierra Leone</TyOption>
              <TyOption value="sg">🇸🇬 Singapore</TyOption>
              <TyOption value="sk">🇸🇰 Slovakia</TyOption>
              <TyOption value="si">🇸🇮 Slovenia</TyOption>
              <TyOption value="sb">🇸🇧 Solomon Islands</TyOption>
              <TyOption value="so">🇸🇴 Somalia</TyOption>
              <TyOption value="za">🇿🇦 South Africa</TyOption>
              <TyOption value="ss">🇸🇸 South Sudan</TyOption>
              <TyOption value="es">🇪🇸 Spain</TyOption>
              <TyOption value="lk">🇱🇰 Sri Lanka</TyOption>
              <TyOption value="sd">🇸🇩 Sudan</TyOption>
              <TyOption value="sr">🇸🇷 Suriname</TyOption>
              <TyOption value="sz">🇸🇿 Eswatini</TyOption>
              <TyOption value="se">🇸🇪 Sweden</TyOption>
              <TyOption value="ch">🇨🇭 Switzerland</TyOption>
              <TyOption value="sy">🇸🇾 Syria</TyOption>
              <TyOption value="tw">🇹🇼 Taiwan</TyOption>
              <TyOption value="tj">🇹🇯 Tajikistan</TyOption>
              <TyOption value="tz">🇹🇿 Tanzania</TyOption>
              <TyOption value="th">🇹🇭 Thailand</TyOption>
              <TyOption value="tl">🇹🇱 Timor-Leste</TyOption>
              <TyOption value="tg">🇹🇬 Togo</TyOption>
              <TyOption value="to">🇹🇴 Tonga</TyOption>
              <TyOption value="tt">🇹🇹 Trinidad and Tobago</TyOption>
              <TyOption value="tn">🇹🇳 Tunisia</TyOption>
              <TyOption value="tr">🇹🇷 Turkey</TyOption>
              <TyOption value="tm">🇹🇲 Turkmenistan</TyOption>
              <TyOption value="tv">🇹🇻 Tuvalu</TyOption>
              <TyOption value="ug">🇺🇬 Uganda</TyOption>
              <TyOption value="ua">🇺🇦 Ukraine</TyOption>
              <TyOption value="ae">🇦🇪 United Arab Emirates</TyOption>
              <TyOption value="gb">🇬🇧 United Kingdom</TyOption>
              <TyOption value="us">🇺🇸 United States</TyOption>
              <TyOption value="uy">🇺🇾 Uruguay</TyOption>
              <TyOption value="uz">🇺🇿 Uzbekistan</TyOption>
              <TyOption value="vu">🇻🇺 Vanuatu</TyOption>
              <TyOption value="va">🇻🇦 Vatican City</TyOption>
              <TyOption value="ve">🇻🇪 Venezuela</TyOption>
              <TyOption value="vn">🇻🇳 Vietnam</TyOption>
              <TyOption value="ye">🇾🇪 Yemen</TyOption>
              <TyOption value="zm">🇿🇲 Zambia</TyOption>
              <TyOption value="zw">🇿🇼 Zimbabwe</TyOption>
            </TyDropdown>

            <p style={{ marginTop: '1rem', fontSize: '14px', color: '#374151' }}>
              Selected: <code style={{ background: '#f3f4f6', padding: '2px 6px', borderRadius: '4px' }}>
                {dropdownValues.countries || 'none'}
              </code>
            </p>
          </div>

          {/* Second Large Dropdown for Simultaneous Test */}
          <div style={{
            padding: '1.5rem',
            background: 'white',
            borderRadius: '8px',
            boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
          }}>
            <h3 style={{ marginTop: 0, color: '#1f2937' }}>🏢 Companies (Simultaneous Test)</h3>
            <p style={{ color: '#6b7280', fontSize: '14px', marginBottom: '1rem' }}>
              <strong>Test simultaneous dropdowns:</strong> Open this dropdown while the first one is also open.<br />
              Body scroll should remain locked until both are closed.
            </p>

            <TyDropdown
              placeholder="Search companies..."
              label="Select Company"
              value={dropdownValues.countries2}
              searchable
              flavor="success"
              style={{ minWidth: '280px' }}
              onChange={(e) => handleDropdownChange('countries2', e)}
            >
              <TyOption value="microsoft">🏢 Microsoft Corporation</TyOption>
              <TyOption value="apple">🍎 Apple Inc.</TyOption>
              <TyOption value="google">🔍 Google LLC</TyOption>
              <TyOption value="amazon">📦 Amazon.com Inc.</TyOption>
              <TyOption value="meta">👥 Meta Platforms Inc.</TyOption>
              <TyOption value="tesla">⚡ Tesla Inc.</TyOption>
              <TyOption value="netflix">🎬 Netflix Inc.</TyOption>
              <TyOption value="uber">🚗 Uber Technologies</TyOption>
              <TyOption value="airbnb">🏠 Airbnb Inc.</TyOption>
              <TyOption value="spotify">🎵 Spotify Technology</TyOption>
              <TyOption value="adobe">🎨 Adobe Inc.</TyOption>
              <TyOption value="salesforce">☁️ Salesforce Inc.</TyOption>
              <TyOption value="oracle">🔴 Oracle Corporation</TyOption>
              <TyOption value="ibm">💙 IBM Corporation</TyOption>
              <TyOption value="intel">💻 Intel Corporation</TyOption>
              <TyOption value="cisco">🌐 Cisco Systems Inc.</TyOption>
              <TyOption value="zoom">📹 Zoom Video Communications</TyOption>
              <TyOption value="slack">💬 Slack Technologies</TyOption>
              <TyOption value="github">🐙 GitHub Inc.</TyOption>
              <TyOption value="gitlab">🦊 GitLab Inc.</TyOption>
              <TyOption value="atlassian">🔵 Atlassian Corporation</TyOption>
              <TyOption value="shopify">🛒 Shopify Inc.</TyOption>
              <TyOption value="square">⬜ Block Inc. (Square)</TyOption>
              <TyOption value="stripe">💳 Stripe Inc.</TyOption>
              <TyOption value="paypal">💰 PayPal Holdings</TyOption>
              <TyOption value="visa">💎 Visa Inc.</TyOption>
              <TyOption value="mastercard">🔴 Mastercard Inc.</TyOption>
              <TyOption value="twitter">🐦 X Corp. (Twitter)</TyOption>
              <TyOption value="linkedin">💼 LinkedIn Corporation</TyOption>
              <TyOption value="pinterest">📌 Pinterest Inc.</TyOption>
              <TyOption value="snapchat">👻 Snap Inc.</TyOption>
              <TyOption value="tiktok">🎵 TikTok (ByteDance)</TyOption>
              <TyOption value="discord">🎮 Discord Inc.</TyOption>
              <TyOption value="twitch">🎮 Twitch Interactive</TyOption>
              <TyOption value="reddit">🤖 Reddit Inc.</TyOption>
              <TyOption value="dropbox">📁 Dropbox Inc.</TyOption>
              <TyOption value="box">📦 Box Inc.</TyOption>
              <TyOption value="docusign">📝 DocuSign Inc.</TyOption>
              <TyOption value="okta">🔐 Okta Inc.</TyOption>
              <TyOption value="datadog">🐕 Datadog Inc.</TyOption>
              <TyOption value="mongodb">🍃 MongoDB Inc.</TyOption>
              <TyOption value="snowflake">❄️ Snowflake Inc.</TyOption>
              <TyOption value="palantir">🔮 Palantir Technologies</TyOption>
              <TyOption value="crowdstrike">🛡️ CrowdStrike Holdings</TyOption>
              <TyOption value="zscaler">🔒 Zscaler Inc.</TyOption>
              <TyOption value="cloudflare">☁️ Cloudflare Inc.</TyOption>
              <TyOption value="fastly">⚡ Fastly Inc.</TyOption>
              <TyOption value="akamai">🌐 Akamai Technologies</TyOption>
              <TyOption value="vmware">💻 VMware Inc.</TyOption>
              <TyOption value="nutanix">🔧 Nutanix Inc.</TyOption>
              <TyOption value="docker">🐳 Docker Inc.</TyOption>
              <TyOption value="kubernetes">☸️ Cloud Native Computing Foundation</TyOption>
              <TyOption value="hashicorp">🔧 HashiCorp Inc.</TyOption>
              <TyOption value="jetbrains">🧠 JetBrains s.r.o.</TyOption>
              <TyOption value="figma">🎨 Figma Inc.</TyOption>
              <TyOption value="canva">🎨 Canva Pty Ltd.</TyOption>
              <TyOption value="notion">📝 Notion Labs Inc.</TyOption>
              <TyOption value="airtable">📊 Airtable Inc.</TyOption>
              <TyOption value="asana">✅ Asana Inc.</TyOption>
              <TyOption value="trello">📋 Trello (Atlassian)</TyOption>
              <TyOption value="jira">🐛 Jira (Atlassian)</TyOption>
              <TyOption value="confluence">📖 Confluence (Atlassian)</TyOption>
            </TyDropdown>

            <p style={{ marginTop: '1rem', fontSize: '14px', color: '#374151' }}>
              Selected: <code style={{ background: '#f3f4f6', padding: '2px 6px', borderRadius: '4px' }}>
                {dropdownValues.countries2 || 'none'}
              </code>
            </p>
          </div>
        </div>

        {/* Status Display */}
        <div style={{
          padding: '1rem',
          background: '#dcfce7',
          border: '1px solid #16a34a',
          borderRadius: '8px',
          marginBottom: '1rem'
        }}>
          <h4 style={{ color: '#15803d', marginTop: 0 }}>🎯 Current Test Status</h4>
          <div style={{ display: 'grid', gap: '0.5rem', fontSize: '14px' }}>
            <div>
              <strong>Countries Selected:</strong> {dropdownValues.countries || 'None'}
            </div>
            <div>
              <strong>Company Selected:</strong> {dropdownValues.countries2 || 'None'}
            </div>
            <div style={{ marginTop: '0.5rem', fontFamily: 'monospace', background: '#f0fdf4', padding: '0.5rem', borderRadius: '4px' }}>
              <strong>Expected Behavior:</strong><br />
              • Dropdown open → Body scroll locked ❌<br />
              • Dropdown internal scroll → Works ✅<br />
              • Dropdown closed → Body scroll restored ✅<br />
              • Multiple dropdowns → Body locked until all closed ❌
            </div>
          </div>
        </div>
      </div>

      {/* SPACER CONTENT TO MAKE PAGE SCROLLABLE */}
      <div style={{
        padding: '2rem',
        background: 'linear-gradient(45deg, #f3f4f6 25%, transparent 25%), linear-gradient(-45deg, #f3f4f6 25%, transparent 25%), linear-gradient(45deg, transparent 75%, #f3f4f6 75%), linear-gradient(-45deg, transparent 75%, #f3f4f6 75%)',
        backgroundSize: '20px 20px',
        backgroundPosition: '0 0, 0 10px, 10px -10px, -10px 0px',
        marginBottom: '2rem',
        borderRadius: '8px'
      }}>
        <h2>📄 Page Content (Makes Body Scrolling Obvious)</h2>
        <p>This section adds content to make the page scrollable, so you can clearly test whether body scrolling is locked when dropdowns are open.</p>

        {/* Generate lots of content */}
        {Array.from({ length: 20 }, (_, i) => (
          <div key={i} style={{
            margin: '1rem 0',
            padding: '1rem',
            background: 'white',
            borderRadius: '8px',
            border: '1px solid #e5e7eb'
          }}>
            <h3>Section {i + 1}: Lorem Ipsum Content</h3>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
            <p>Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>

            {i % 5 === 0 && (
              <div style={{ marginTop: '1rem' }}>
                <TyButton
                  flavor={i % 3 === 0 ? 'primary' : i % 3 === 1 ? 'success' : 'warning'}
                  onClick={() => handleButtonClick(`section-${i + 1}`)}
                >
                  Test Button in Section {i + 1}
                </TyButton>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* ORIGINAL LAYOUT DEBUG TEST */}
      <div className="demo-section">
        <h2>🔬 Layout Contamination Debug Test</h2>
        <p><strong>Test Hypothesis:</strong> Fixed-width containers should isolate dropdown layout effects</p>

        {/* Test Grid with Fixed Width Containers */}
        <div style={{
          display: 'grid',
          gap: '2rem',
          gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
          marginBottom: '2rem',
          border: '2px dashed #999',
          padding: '1rem',
          background: '#f0f0f0'
        }}>
          {/* Fixed Width Container 1 */}
          <div style={{
            width: '350px',           // FIXED WIDTH
            height: '200px',          // FIXED HEIGHT
            border: '2px solid red',
            padding: '1rem',
            background: 'white',
            overflow: 'visible'       // Allow dropdown to overflow
          }}>
            <h3 style={{ margin: '0 0 1rem 0', fontSize: '16px' }}>Fixed Container 1</h3>
            <p style={{ margin: '0 0 1rem 0', fontSize: '14px', color: '#666' }}>Width: 350px (fixed)</p>

            <TyDropdown
              placeholder="Select language..."
              label="Language"
              flavor="primary"
              value={dropdownValues.isolated1}
              searchable
              onChange={(e) => handleDropdownChange('isolated1', e)}
            >
              <TyOption value="javascript">JavaScript</TyOption>
              <TyOption value="typescript">TypeScript</TyOption>
              <TyOption value="clojure">Clojure</TyOption>
              <TyOption value="rust">Rust</TyOption>
              <TyOption value="python">Python</TyOption>
            </TyDropdown>

            <p style={{ marginTop: '1rem', fontSize: '12px' }}>
              Selected: <code>{dropdownValues.isolated1 || 'none'}</code>
            </p>
          </div>

          {/* Fixed Width Container 2 */}
          <div style={{
            width: '350px',           // FIXED WIDTH
            height: '200px',          // FIXED HEIGHT
            border: '2px solid blue',
            padding: '1rem',
            background: 'white',
            overflow: 'visible'       // Allow dropdown to overflow
          }}>
            <h3 style={{ margin: '0 0 1rem 0', fontSize: '16px' }}>Fixed Container 2</h3>
            <p style={{ margin: '0 0 1rem 0', fontSize: '14px', color: '#666' }}>Width: 350px (fixed)</p>

            <TyDropdown
              placeholder="Select fruit..."
              label="Fruit"
              flavor="success"
              value={dropdownValues.isolated2}
              options={fruitOptions}
              onChange={(e) => handleDropdownChange('isolated2', e)}
            />

            <p style={{ marginTop: '1rem', fontSize: '12px' }}>
              Selected: <code>{dropdownValues.isolated2 || 'none'}</code>
            </p>
          </div>

          {/* Fixed Width Container 3 - NO DROPDOWN (Control) */}
          <div style={{
            width: '350px',           // FIXED WIDTH
            height: '200px',          // FIXED HEIGHT
            border: '2px solid green',
            padding: '1rem',
            background: 'white'
          }}>
            <h3 style={{ margin: '0 0 1rem 0', fontSize: '16px' }}>Control Container</h3>
            <p style={{ margin: '0 0 1rem 0', fontSize: '14px', color: '#666' }}>Width: 350px (fixed)</p>
            <p style={{ fontSize: '14px' }}>No dropdown - this container should <strong>never</strong> change size when other dropdowns open.</p>

            <TyButton flavor="neutral" size="sm">Static Button</TyButton>
          </div>

          {/* Fixed Width Container 4 - TEAM DROPDOWN */}
          <div style={{
            width: '350px',           // FIXED WIDTH
            height: '200px',          // FIXED HEIGHT
            border: '2px solid purple',
            padding: '1rem',
            background: 'white',
            overflow: 'visible'       // Allow dropdown to overflow
          }}>
            <h3 style={{ margin: '0 0 1rem 0', fontSize: '16px' }}>Rich Content Test</h3>
            <p style={{ margin: '0 0 1rem 0', fontSize: '14px', color: '#666' }}>Width: 350px (fixed)</p>

            <TyDropdown
              placeholder="Assign to..."
              label="Team"
              flavor="warning"
              value={dropdownValues.team}
              onChange={(e) => handleDropdownChange('team', e)}
            >
              <TyOption value="alice">
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <div style={{
                    width: '24px', height: '24px', borderRadius: '50%',
                    background: '#3b82f6', display: 'flex', alignItems: 'center',
                    justifyContent: 'center', color: '#fff', fontSize: '12px', fontWeight: '500'
                  }}>A</div>
                  <span>Alice Johnson</span>
                </div>
              </TyOption>
              <TyOption value="bob">
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <div style={{
                    width: '24px', height: '24px', borderRadius: '50%',
                    background: '#8b5cf6', display: 'flex', alignItems: 'center',
                    justifyContent: 'center', color: '#fff', fontSize: '12px', fontWeight: '500'
                  }}>B</div>
                  <span>Bob Smith</span>
                </div>
              </TyOption>
            </TyDropdown>
          </div>
        </div>
      </div>

      {/* MORE FILLER CONTENT */}
      <div style={{
        padding: '2rem',
        background: 'linear-gradient(135deg, #fef3c7 0%, #fed7aa 100%)',
        marginBottom: '2rem',
        borderRadius: '8px'
      }}>
        <h2>🎯 Final Test Results</h2>
        <p>After testing the dropdowns above, the scroll lock system should:</p>
        <ul>
          <li>✅ <strong>Lock body scrolling</strong> when any dropdown is open</li>
          <li>✅ <strong>Allow internal scrolling</strong> within dropdown options lists</li>
          <li>✅ <strong>Support multiple dropdowns</strong> being open simultaneously</li>
          <li>✅ <strong>Restore body scrolling</strong> only when all dropdowns are closed</li>
          <li>✅ <strong>Prevent layout shifts</strong> - no "twitchy" behavior</li>
          <li>✅ <strong>Work with keyboard navigation</strong> and search filtering</li>
        </ul>
      </div>

      <div className="demo-section">
        <h2>Event Log</h2>
        <div style={{ marginTop: '1rem', padding: '1rem', background: 'var(--ty-bg-neutral-soft)' }}>
          <h3>Dropdown Event Test Results:</h3>
          {dropdownEvents.length > 0 ? (
            <ul style={{ margin: 0, paddingLeft: '1.5rem' }}>
              {dropdownEvents.map((event, index) => (
                <li key={index}>{event}</li>
              ))}
            </ul>
          ) : (
            <p>No dropdown events yet. Try selecting options from the dropdowns above!</p>
          )}
        </div>
      </div>

      {/* FINAL SPACER TO ENSURE PAGE IS VERY SCROLLABLE */}
      <div style={{ height: '500px', background: 'linear-gradient(to bottom, #e0e7ff, #c7d2fe)', display: 'flex', alignItems: 'center', justifyContent: 'center', borderRadius: '8px' }}>
        <div style={{ textAlign: 'center' }}>
          <h2>🏁 End of Page</h2>
          <p>If you can see this, the page is definitely scrollable!</p>
          <p>Scroll back up to test the dropdowns.</p>
        </div>
      </div>
    </div>
  )
}

export default App
