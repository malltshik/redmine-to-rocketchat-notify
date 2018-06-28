import React, {Component} from 'react';

class Employee extends Component {

    render() {
        let emp = this.props.model;
        return (
            <p style={{padding: "10px"}}>{emp.firstName} {emp.lastName}</p>
        )
    }
}

export default Employee;
