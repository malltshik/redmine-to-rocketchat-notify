import React, {Component} from 'react';
import Employee from "./Employee";
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import Switch from '@material-ui/core/Switch';
import Gravatar from 'react-gravatar'
import Tooltip from '@material-ui/core/Tooltip';

class Employees extends Component {
    render() {
        const { employees } = this.props;
        return (
            <div>
                <List>
                    {employees.map((model) => (
                        <ListItem key={model.id}>
                            <Gravatar email={model.username + "@tecforce.ru"} rating="pg" default="identicon"/>
                            <Employee model={model}/>
                            <ListItemSecondaryAction>
                                <Tooltip id="tooltip-icon" title="Enable/Disable Notifications">
                                    <Switch
                                        onChange={(e) => this.props.onToggleNotification(model, e) }
                                        defaultChecked={model.notificationEnable}
                                    />
                                </Tooltip>
                            </ListItemSecondaryAction>
                        </ListItem>
                    ))}
                </List>
            </div>
        )
    }
}

export default Employees;
