import React, {Component} from 'react';
import PropTypes from 'prop-types';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import {withStyles} from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Logo from '../../assets/images/logo.svg'

const styles = {
    root: {
        flexGrow: 1,
    },
    flex: {
        flex: 1,
        margin: "0"
    },
};

class Header extends Component {

    render() {
        const {classes} = this.props;
        return (
            <AppBar position="sticky" color="default">
                <Toolbar>
                    <img src={Logo} alt="Logo" style={{paddingRight: "20px", width: "50px"}}/>
                    <TextField disabled={true} onChange={this.props.onSearch} className={classes.flex}
                               placeholder="// TODO Search" margin="none"/>
                </Toolbar>
            </AppBar>
        );
    }
}

Header.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Header);
