import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'book',
        data: { pageTitle: 'jhipster7App.book.home.title' },
        loadChildren: () => import('./book/book.module').then(m => m.BookModule),
      },
      {
        path: 'author',
        data: { pageTitle: 'jhipster7App.author.home.title' },
        loadChildren: () => import('./author/author.module').then(m => m.AuthorModule),
      },
      {
        path: 'community-images',
        data: { pageTitle: 'jhipster7App.communityImages.home.title' },
        loadChildren: () => import('./community-images/community-images.module').then(m => m.CommunityImagesModule),
      },
      {
        path: 'community-image-group',
        data: { pageTitle: 'jhipster7App.communityImageGroup.home.title' },
        loadChildren: () => import('./community-image-group/community-image-group.module').then(m => m.CommunityImageGroupModule),
      },
      {
        path: 'test-01',
        data: { pageTitle: 'jhipster7App.test01.home.title' },
        loadChildren: () => import('./test-01/test-01.module').then(m => m.Test01Module),
      },
      {
        path: 'community-leader',
        data: { pageTitle: 'jhipster7App.communityLeader.home.title' },
        loadChildren: () => import('./community-leader/community-leader.module').then(m => m.CommunityLeaderModule),
      },
      {
        path: 'community',
        data: { pageTitle: 'jhipster7App.community.home.title' },
        loadChildren: () => import('./community/community.module').then(m => m.CommunityModule),
      },
      {
        path: 'community-notice',
        data: { pageTitle: 'jhipster7App.communityNotice.home.title' },
        loadChildren: () => import('./community-notice/community-notice.module').then(m => m.CommunityNoticeModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'jhipster7App.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'buildings',
        data: { pageTitle: 'jhipster7App.buildings.home.title' },
        loadChildren: () => import('./buildings/buildings.module').then(m => m.BuildingsModule),
      },
      {
        path: 'homeland-station',
        data: { pageTitle: 'jhipster7App.homelandStation.home.title' },
        loadChildren: () => import('./homeland-station/homeland-station.module').then(m => m.HomelandStationModule),
      },
      {
        path: 'room-addr',
        data: { pageTitle: 'jhipster7App.roomAddr.home.title' },
        loadChildren: () => import('./room-addr/room-addr.module').then(m => m.RoomAddrModule),
      },
      {
        path: 'tu-ya-cmd',
        data: { pageTitle: 'jhipster7App.tuYaCmd.home.title' },
        loadChildren: () => import('./tu-ya-cmd/tu-ya-cmd.module').then(m => m.TuYaCmdModule),
      },
      {
        path: 'tu-ya-device',
        data: { pageTitle: 'jhipster7App.tuYaDevice.home.title' },
        loadChildren: () => import('./tu-ya-device/tu-ya-device.module').then(m => m.TuYaDeviceModule),
      },
      {
        path: 'visitor',
        data: { pageTitle: 'jhipster7App.visitor.home.title' },
        loadChildren: () => import('./visitor/visitor.module').then(m => m.VisitorModule),
      },
      {
        path: 'wamoli-face-library',
        data: { pageTitle: 'jhipster7App.wamoliFaceLibrary.home.title' },
        loadChildren: () => import('./wamoli-face-library/wamoli-face-library.module').then(m => m.WamoliFaceLibraryModule),
      },
      {
        path: 'wamoli-user',
        data: { pageTitle: 'jhipster7App.wamoliUser.home.title' },
        loadChildren: () => import('./wamoli-user/wamoli-user.module').then(m => m.WamoliUserModule),
      },
      {
        path: 'wamoli-user-location',
        data: { pageTitle: 'jhipster7App.wamoliUserLocation.home.title' },
        loadChildren: () => import('./wamoli-user-location/wamoli-user-location.module').then(m => m.WamoliUserLocationModule),
      },
      {
        path: 'company-dept',
        data: { pageTitle: 'jhipster7App.companyDept.home.title' },
        loadChildren: () => import('./company-dept/company-dept.module').then(m => m.CompanyDeptModule),
      },
      {
        path: 'company-post',
        data: { pageTitle: 'jhipster7App.companyPost.home.title' },
        loadChildren: () => import('./company-post/company-post.module').then(m => m.CompanyPostModule),
      },
      {
        path: 'company-user',
        data: { pageTitle: 'jhipster7App.companyUser.home.title' },
        loadChildren: () => import('./company-user/company-user.module').then(m => m.CompanyUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
