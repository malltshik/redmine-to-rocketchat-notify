import React, {Component} from 'react';
import Employee from "./Employee";


class Employees extends Component {

    render() {
        const {employees} = this.props;
        return (
            <div style={{marginTop: "10px"}}>
                {employees.map((model) => (
                    <Employee key={model.id} model={model} parentProps={this.props}/>
                ))}
            </div>
        )
    }
}

export default Employees;
