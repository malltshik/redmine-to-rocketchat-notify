import React, {Component} from 'react';

class Employee extends Component {

    render() {
        return (
            <p>{this.props.model.name}</p>
        )
    }

}
export default Employee;
