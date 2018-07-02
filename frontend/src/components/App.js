import React, {Component} from 'react';
import Header from "./header/Header";
import Employees from "./employees/Employees"
import rest from "../services/rest"

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {query: "", employees: [], filtred: []};
    }

    onSearch(event) {
        const {value} = event.target;
        this.setState({
            query: value,
            filtred: this.state.employees.filter(
                function (item) {
                    if (value) return (
                        item.username.toLowerCase().indexOf(value) !== -1 ||
                        item.firstName.toLowerCase().indexOf(value) !== -1 ||
                        item.lastName.toLowerCase().indexOf(value) !== -1
                    );
                    else return true
                })
        });
    };


    handleToggle(model) {
        rest.get(`/employees/${model.id}/toggleNotify/`)
            .then(resp => this.state.employees[this.state.employees.findIndex(e => e.id === model.id)] = resp)
            .catch(error => console.error("Unable to toggle notification setting on/off", error));
    };

    handleRequiredTimeChange(model, event) {
        rest.get(`/employees/${model.id}/setRequiredTimeToLog/?timeToLog=${event.target.value}`)
            .then(resp => this.state.employees[this.state.employees.findIndex(e => e.id === model.id)] = resp)
            .catch(error => console.error("Unable to change required time to log", error));
    }

    componentWillMount() {
        rest.get("/employees/")
            .then(resp => {
                this.setState({employees: resp.data, filtred: resp.data});
            }).catch(resp => {
            this.setState({employees: [], filtred: [], loaded: false, message: resp.data.message})
        });
    }

    render() {
        return (
            <div>
                <Header onSearch={this.onSearch.bind(this)}/>
                <Employees employees={this.state.filtred}
                           onToggleNotification={this.handleToggle.bind(this)}
                           onRequiredTimeChange={this.handleRequiredTimeChange.bind(this)}/>
            </div>
        );
    }
}

export default App;
