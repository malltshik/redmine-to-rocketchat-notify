import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './components/App';
import registerServiceWorker from './registerServiceWorker';
import {createMuiTheme, MuiThemeProvider} from '@material-ui/core/styles';
import teal from '@material-ui/core/colors/teal';
import lightBlue from '@material-ui/core/colors/lightBlue';

const theme = createMuiTheme({
    palette: {
        primary: teal,
        secondary: lightBlue
    }
});

ReactDOM.render(<MuiThemeProvider theme={theme}><App/></MuiThemeProvider>, document.getElementById('root'));
registerServiceWorker();
