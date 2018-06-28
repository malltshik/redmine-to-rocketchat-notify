import React, {Component} from 'react';
import Header from "./header/Header";
import Employees from "./employees/Employees"
import rest from "../services/rest"

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {query: "", employees: []};
        this.filter.bind(this)
    }

    onSearch(event) {
        this.setState({query: event.target.value})
    };

    handleToggle(model, event){
        rest.get(`/users/${model.id}/toggleNotify`)
            .then(resp => {model = resp.data})
            .catch(error => console.error("Unable to toggle notification setting on/off", error));
    };

    filter(employees, query) {
        return employees.filter(function (item) {
            if (query) return (
                item.username.toLowerCase().indexOf(query) !== -1 ||
                item.firstName.toLowerCase().indexOf(query) !== -1 ||
                item.lastName.toLowerCase().indexOf(query) !== -1
            );
            else return true
        });
    }

    componentWillMount() {
        rest.get("/users/")
            .then(resp => {
                this.setState({ employees: resp.data });
            }).catch(resp => {
            this.setState({employees: [], loaded: false, message: resp.data.message})
        });
    }

    render() {
        return (
            <div>
                <Header onSearch={this.onSearch.bind(this)}/>
                <Employees employees={this.filter(this.state.employees, this.state.query)}
                           onToggleNotification={this.handleToggle.bind(this)}/>
            </div>
        );
    }
}

export default App;
