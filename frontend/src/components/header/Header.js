import React, {Component} from 'react';
import PropTypes from 'prop-types';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import {withStyles} from '@material-ui/core/styles';
import Input from '@material-ui/core/Input';
import InputAdornment from '@material-ui/core/InputAdornment';
import FormControl from '@material-ui/core/FormControl';
import SearchIcon from "@material-ui/icons/Search";

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
                    {/*<img src={Logo} alt="Logo" style={{paddingRight: "20px", width: "50px"}}/>*/}
                    <FormControl className={classes.flex}>
                        <Input onChange={this.props.onSearch}
                               placeholder="Начните вводить логин, имя или фамилию сотрудника"
                               startAdornment={
                                   <InputAdornment position="start">
                                       <SearchIcon color={"primary"}/>
                                   </InputAdornment>
                               }
                        />
                    </FormControl>
                </Toolbar>
            </AppBar>
        );
    }
}

Header.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Header);
