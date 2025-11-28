export const environment = {
    production: false,
    apiUrl: 'http://localhost:8080/api',


    endpoints: {
        device: {
          base: '/device',
          getAll: '/device/getdevice',
          create: '/device/create',
          delete: (id: number) => `/device/delete/${id}`,
          count: '/device/count',
          searchAvailable: '/device/search-available',
          getAvailableDevice : '/device/available-devices',
          updateDevice: (deviceId : number) => `/device/update/${deviceId}`,
          fileterDevice: '/device/filterdevice'
        },

        reservation: {
            base: '/reservations',
            create: '/reservations/create',
            getByUser: (UserId: number) => `/reservations/getreservation/${UserId}`,
            getstatuspending: '/reservations/getstatuspending',
            deleteRs: (reservationId: number) => `/reservations/deleters/${reservationId}`,
            updateStatus: (reservationId: number) => `/reservations/updatestatus/${reservationId}`,
            filterReservation: '/reservations/filter',
            updateReservation:(reservationId : number) => `/reservations/updatereservation/${reservationId}`
          },

          maintenance:{
            countmaintenance : ('/maintenance/count')
          },
      
          category: {
            base: '/categories',
            getAll: '/categories/getall'
          },
      
          classroom: {
            base: '/classrooms',
            getAll: '/classrooms/getallroom'
          },

          user: {
            base:'/auth',
            login:'/auth/login',
            register : '/auth/register',
            getuser:'/auth/getuser',
            delete: (userId: number) => `/auth/delete/${userId}`,
          },

          borrow:{
            base : '/borrow',
            crateBorrow : '/borrow/createnow',
            countborrow : '/borrow/countborrow',
            returnborrow: (borrowId : number) =>`/borrow/returnborrow/${borrowId}`,
            getBorrowById:(userId : number) =>`/borrow/getborrowbyid/${userId}`,
            ExtendBorrow :(reservationId : number) =>`/borrow/extend/${reservationId}`,
            countoverdue : '/borrow/countoverdue',
            getalmostDue : '/borrow/almostDue',
            getallborrow : '/borrow/getborrow',
            countTotalBorrow :'/borrow/counttotalborrow',
            countreturnborrow : '/borrow/countborrowreturn'
          }
    }
}