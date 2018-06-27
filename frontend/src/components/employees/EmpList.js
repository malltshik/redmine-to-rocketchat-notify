import React, {Component} from 'react';
import axios from 'axios';
import Employee from "./Employee";

class EmpList extends Component {

    constructor(props) {
        super(props);
        this.state = {employees: []}
    }

    componentDidMount() {
        axios.get("/api/users/")
        .then(resp => {
            this.setState({employees: resp.data})
        }).catch(resp => {
            this.setState({employees: [], loaded: false, message: resp.data.message })
        })
    }

    render() {
        return (
            <ul>
                {
                    this.state.employees.forEach(emp => {
                        return <li><Employee model={emp}/></li>
                    })
                }
            </ul>
        )
    }
}

export default EmpList;
