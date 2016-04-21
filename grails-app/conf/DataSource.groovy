dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

// environment specific settings
environments {
    development {
        dataSource {
            //dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            pooled = true
           //dbCreate = "update"
			dbCreate="update"
		 //url="jdbc:mysql://localhost/rhkyo?autoReconnect=true"
		url="jdbc:mysql://localhost/rh?autoReconnect=true"
		 // url = "jdbc:mysql://10.10.1.228/rh?autoReconnect=true"
			//url="jdbc:mysql://10.10.6.1/rh?autoReconnect=true" //Kyo
            driverClassName = "com.mysql.jdbc.Driver"
            dialect = org.hibernate.dialect.MySQL5InnoDBDialect
            username = "root"
            password = "sys"
            properties {
                maxActive = 3
                maxIdle = 2
                minIdle = 1
                initialSize = 1
				minEvictableIdleTimeMillis=1800000
				timeBetweenEvictionRunsMillis=1800000
				numTestsPerEvictionRun=3
				testOnBorrow=true
				testWhileIdle=true
				testOnReturn=true
				maxWait = 10000
            }
            
            
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
		dataSource {
			//dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
			//url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
			pooled = true
		   //dbCreate = "update"
			dbCreate="update"
			url="jdbc:mysql://localhost/rh?autoReconnect=true"
			//url="jdbc:mysql://10.10.6.1/rh?autoReconnect=true" //Kyo
			//url="jdbc:mysql://10.10.1.228/rh?autoReconnect=true"
			driverClassName = "com.mysql.jdbc.Driver"
			dialect = org.hibernate.dialect.MySQL5InnoDBDialect
			username = "root"
			password = "sys"
			properties {
				maxActive = 10
                maxIdle = 10
                minIdle = 3
                initialSize = 3
				minEvictableIdleTimeMillis=1800000
				timeBetweenEvictionRunsMillis=1800000
				numTestsPerEvictionRun=3
				testOnBorrow=true
				testWhileIdle=true
				testOnReturn=true
				maxWait = 10000
			}
			
			
		}
    }
}
