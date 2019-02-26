var json = {
	dataSource : {
		type : "com.mchange.v2.c3p0.ComboPooledDataSource",
		fields : {
			driverClass : "com.mysql.jdbc.Driver",
			jdbcUrl : "jdbc:mysql://192.168.80.119:3306/zndc?useUnicode=true&characterEncoding=UTF-8",
			user : "gtplus",
			password : "123123123",
			initialPoolSize : 15,
			minPoolSize : 10, 
			maxPoolSize : 30,
			maxIdleTime : 120,
			maxConnectionAge : 180,
			maxStatements : 20,
			acquireIncrement : 5,
			acquireRetryAttempts : 30,
			acquireRetryDelay : 1000,
			checkoutTimeout : 20000, 
			numHelperThreads : 2
		},
		events : {
			depose : 'close'
		}
	},
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		args : [{refer : "dataSource"}]
	}
};