// main.jsx
import { StrictMode } from 'react'
import { Provider } from 'react-redux';
import { store } from './store';
import { createRoot } from 'react-dom/client'
import './static/css/common.scss';
import './static/css/basic.scss';
import Root from './router/Root';
import AppInit from './Components/AppInit';

createRoot(document.getElementById('root')).render(
  //<StrictMode>
    <Provider store={store}>
      <AppInit/>
      <Root/>
    </Provider>
  //</StrictMode>
)