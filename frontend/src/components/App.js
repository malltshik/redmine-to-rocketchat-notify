import React, {Component} from 'react';
import Header from "./header/Header";
import EmpList from "./employees/EmpList";

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
                <Header />
                <EmpList />
            </div>
        );
    }
}

export default App;
