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


class Search extends Component {

    constructor(props) {
        super(props);
        this.lastClick = null;
        this.state = {show: false, searchQuery: ""};
        this.showSearchPanel.bind(this)
    }

    showSearchPanel(event) {
        if (event.key === 's') {
            if (Date.now() - this.lastClick < 300) {
                this.setState({show: true})
                document.getElementById("search-field").focus()
            } else {
                this.lastClick = Date.now();
            }
        }
        if(event.key === 'Escape' && this.state.show) {
            this.setState({show: false});
        }
    }

    componentWillMount() {
        document.addEventListener("keypress", this.showSearchPanel.bind(this));
    }

    render() {
        const {classes, onSearch} = this.props;
        return (
            <AppBar style={{display: this.state.show ? "block" : "none"}} position="sticky" color="default">
                <Toolbar>
                    {/*<img src={Logo} alt="Logo" style={{paddingRight: "20px", width: "50px"}}/>*/}
                    <FormControl className={classes.flex}>
                        <Input onChange={onSearch}
                               id="search-field"
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

Search.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles)(Search);
