window.onload = function () {

    // REVISE split into 3 components (login, lobby, leaderboard)
    // TODO error handling (e.g. failing login, join, ...)
    
    Vue.use(FundamentalVue);

    new Vue({
        el: '#app',
        template: `
        <div>
            <FdFormSet v-if="!loggedInUser">
                <FdFormItem label="User"><FdInput v-model="loginUser"/></FdFormItem>
                <FdFormItem label="Password"><FdInput type="password" v-model="loginPassword"/></FdFormItem>
                <FdButton @click="login">Login</FdButton>
            </FdFormSet>
            <FdActionBar v-if="loggedInUser" title="Lobby" :description="loggedInUser.name">
                <FdButton>Leaderboard</FdButton>
                <FdButton styling="emphasized" @click="logout">Logout</FdButton>
            </FdActionBar>
            <FdTable v-if="loggedInUser" :items="tables">      
                <template slot="row" slot-scope="{ item }">
                    <FdTableRow>
                        <FdTableCell>{{ item.id }}</FdTableCell>
                        <FdTableCell>{{ item.players }}</FdTableCell>
                        <FdTableCell>{{ item.stakes }}</FdTableCell>
                        <FdTableCell><FdButton @click="join(item)">Join</FdButton></FdTableCell>
                    </FdTableRow>
                </template>
            </FdTable>
        </div>
        `,
        data: function () {
            const tableHeaders = ['Table Name', 'Players', 'Stakes', ''];
            return {loggedInUser: null, loginUser: '', loginPassword: '', tables: [], headers: tableHeaders};
        },
        created: function () {
            this.checkLoggedIn();
        },
        methods: {
            join: function (table) {
                superagent
                	.post(`api/tables/${table.id}/players`)
                    .send({'playerName': this.loggedInUser.id})
                    .then(function(response) {
                        if (response.status === 204) {
                            window.open(`table/index.html?id=${table.id}`, '_blank');                    
                        }                    	
                    });
            },
            login: function () {
            	var that = this;
                superagent.post('api/login').auth(this.loginUser, this.loginPassword)
                	.send()
                	.then(function(response) {
                        if (response.status === 200) {
                            that.checkLoggedIn();
                        }                		
                	});
            },
            logout: function () {
                superagent.delete('api/login').send().then(function(response) {
                	if (response.status === 204) {
                		location.reload();
                	}
                });
            },
            checkLoggedIn: function () {
            	var that = this;
            	superagent.get('api/login/user').then(function(userResponse) {
                	if (userResponse.status === 200) {
                		that.loggedInUser = userResponse.body;
                		superagent.get('api/tables').then(function(tableResponse) {
                			that.tables = tableResponse.body;                			
                		});
                	}
                });
            }
        }
    });
};