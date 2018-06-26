import React, {Component} from 'react';
import './App.css';

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {name: null};
        this.onChangeInput = this.onChangeInput.bind(this);
    }

    onChangeInput(event) {
        this.setState({name: event.target.value})
    };

    render() {
        return(
            <div>
                <label htmlFor="name">Type your name:</label>
                <input type="text" id="name" onChange={this.onChangeInput}/>
                <br/>
                <h1>Hello { this.state.name || "World" }!</h1>
            </div>
        );
    }
}

export default App;
