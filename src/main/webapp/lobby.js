window.onload = function () {

    //REVISE split into 3 components (login, lobby, leaderboard)
    //TODO error handling (e.g. failing login, join, ...)
    
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
        created: async function () {
            this.checkLoggedIn();
        },
        methods: {
            join: async function (table) {
                const response = await superagent
                    .post(`api/tables/${table.id}/players`)
                    .send({'playerName': this.loggedInUser.id});

                if (response.status === 204) {
                    window.open(`table?id=${table.id}`, '_blank');                    
                }
            },
            login: async function () {
                const response = await superagent.post('api/login').send({
                    'id': this.loginUser,
                    'password': this.loginPassword
                });
                if (response.status === 200) {
                    this.checkLoggedIn();
                }
            },
            logout: async function () {
                const response = await superagent.delete('api/login').send();
                if (response.status === 204) {
                    location.reload();
                }
            },
            checkLoggedIn: async function () {
                const userResponse = await superagent.get('api/login/user');
                if (userResponse.status === 200) {
                    this.loggedInUser = userResponse.body;
                    const tablesResponse = await superagent.get('api/tables');
                    this.tables = tablesResponse.body;
                }
            }
        }
    });
};