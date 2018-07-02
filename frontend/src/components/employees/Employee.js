import React, {Component} from 'react';
import Switch from '@material-ui/core/Switch';
import Gravatar from 'react-gravatar'
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import MenuItem from '@material-ui/core/MenuItem';

class Employee extends Component {

    constructor(props) {
        super(props);
        this.handleChangeTime.bind(this);
        this.onRequiredTimeChange = props.parentProps.onRequiredTimeChange;
        this.state = {requiredHours: [0, 1, 2, 3, 4, 5, 6, 7, 8], selectedHour: props.model.requiredTimeToLog}
    }

    handleChangeTime(model, event) {
        this.onRequiredTimeChange(model, event);
        this.setState({ selectedHour: event.target.value });
    }

    render() {
        const { model, parentProps } = this.props;
        const { selectedHour, requiredHours } = this.state;
        return (
            <ExpansionPanel key={model.id}>
                <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                    <Gravatar email={model.username + "@tecforce.ru"} rating="pg" default="identicon"/>
                    <p style={{paddingLeft: "10px"}}>{model.firstName} {model.lastName}</p>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <div style={{display: "block", width: "100%"}}>
                        <div style={{display: 'inline-flex', width: "100%"}}>
                            <p style={{flex: 1}}>On/Off notifications via Rocket Chat:</p>
                            <div>
                                <Switch
                                    onChange={(e) => parentProps.onToggleNotification(model, e)}
                                    defaultChecked={model.notificationEnable}
                                />
                            </div>
                        </div>
                        <TextField
                            select fullWidth
                            label="Required time to log / Notification limit"
                            value={selectedHour}
                            onChange={(event) => this.handleChangeTime(model, event)}
                            InputProps={{startAdornment: <InputAdornment position="start">Hours</InputAdornment>}}>
                            {requiredHours.map((option, index) => (
                                <MenuItem key={index} value={option}>{option}</MenuItem>
                            ))}
                        </TextField>
                    </div>
                </ExpansionPanelDetails>
            </ExpansionPanel>
        )
    }
}

export default Employee;
