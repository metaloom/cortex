# Loom // Cortex

*Loom // Cortex* is an un-opinionated media processing tool. It can analyze, and parse massive amounts of media efficiently.

It supports a range of functions, including **face detection**, **thumbnail generation**, **consistency checking**, **hashing**, **metadata extraction** and **media fingerprinting**, all of which can be performed in offline and online mode.

*Loom // Cortex* is the central media asset parser component of the *Loom* Headless Media Asset Management System from [MetaLoom](https://metaloom.io/).

## Features 

**Processing** - The ability to process media in offline mode means that it can perform bulk media processing securely at a very large scale.

**Hashing** - One of the standout features of this application is its asset hashing capability, which supports multiple hashing methods such as sha512, sha256, and md5. This enables deduplication of assets, as identical files will produce the same hash value regardless of the hashing method used. By detecting and eliminating duplicate files through hashing, users can save valuable storage space and simplify file management.

**Fingerprinting** - *Loom // Cortex*'s hashing and fingerprinting capabilities enable users to identify unique media content, which is particularly useful for tracking copyrighted material or monitoring user-generated content. Cortex also supports the extraction of metadata, enabling users to organize and search for their content with greater ease.

**Facedetection** - This application offers a powerful face detection feature that can automatically detect and locate faces within media assets. The feature can extract embeddings, which are numerical representations of the facial features that can be used to recognize and compare faces across different media assets. This functionality is particularly useful for applications such as security systems, image search engines, and social media platforms. Detected faces may also be used to automatically compute the optimal focal point for cropped and resized images without manual input.

**Thumbnail** - It includes a thumbnail generation feature that enables users to create and store thumbnail images for media assets. Users can customize the size, format, and quality of the thumbnail images to suit their specific needs. 

**Un-opinionated** - *Loom // Cortex* is considered un-opinionated because it doesn't require the storage, import, or movement of parsed content. Instead, it stores the extracted information alongside the file itself (xattr), without altering the file's location.

**Online Mode** - In online mode it can send the extracted metadata to the *Loom // Nexus* Server. This server is responsible for storing the media data and providing various options for navigating, managing and visualizing the extracted data.

**Consistency checks** - This feature allows users to perform consistency checks on their media assets. It can detect and highlight inconsistencies in the file format, metadata, and content of media assets. By running consistency checks, users can ensure that their media assets are valid and reliable, minimizing the risk of data corruption, file errors, and other issues that may affect the quality of their work.

**Metadata extraction** - *Loom // Cortex* can extract metadata from media assets. This includes information such as file format, resolution, date created, camera settings, and more. By extracting metadata, users can quickly and easily organize and manage their media assets, as well as search and filter them based on specific criteria.

## Deployment

*Loom // Cortex* has a versatile command-line interface (CLI) that allows scheduling and integration in existing workflows.
It can be run via Cron or via a Kubernetes (K8S) Job workload.

## State

* In development

## Releasing 

TBD