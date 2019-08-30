/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { customFieldsType } from './Type-types'
import { stateReferenceType } from './State-types'
import { customerReferenceType } from './Customer-types'
import { loggedResourceType } from './Common-types'
import { customFieldsDraftType } from './Type-types'
import { stateResourceIdentifierType } from './State-types'
import { customerResourceIdentifierType } from './Customer-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'

export function reviewType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             includedInStatistics: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             target: Joi.any().optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             customer: customerReferenceType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             state: stateReferenceType().optional(),
             rating: Joi.number().optional(),
             authorName: Joi.string().optional(),
             key: Joi.string().optional(),
             locale: Joi.string().optional(),
             text: Joi.string().optional(),
             title: Joi.string().optional(),
             uniquenessValue: Joi.string().optional()
          })
}

export function reviewDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             target: Joi.any().optional(),
             custom: customFieldsDraftType().optional(),
             customer: customerResourceIdentifierType().optional(),
             state: stateResourceIdentifierType().optional(),
             rating: Joi.number().optional(),
             authorName: Joi.string().optional(),
             key: Joi.string().optional(),
             locale: Joi.string().optional(),
             text: Joi.string().optional(),
             title: Joi.string().optional(),
             uniquenessValue: Joi.string().optional()
          })
}

export function reviewPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(reviewType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function reviewRatingStatisticsType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             count: Joi.number().required(),
             averageRating: Joi.number().required(),
             highestRating: Joi.number().required(),
             lowestRating: Joi.number().required(),
             ratingsDistribution: Joi.any().required()
          })
}

export function reviewReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('review'),
             id: Joi.string().required(),
             obj: reviewType().optional()
          })
}

export function reviewResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('review'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function reviewUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(reviewUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function reviewUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([reviewSetAuthorNameActionType(), reviewSetCustomFieldActionType(), reviewSetCustomTypeActionType(), reviewSetCustomerActionType(), reviewSetKeyActionType(), reviewSetLocaleActionType(), reviewSetRatingActionType(), reviewSetTargetActionType(), reviewSetTextActionType(), reviewSetTitleActionType(), reviewTransitionStateActionType()])
}

export function reviewSetAuthorNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAuthorName'),
             authorName: Joi.string().optional()
          })
}

export function reviewSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function reviewSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function reviewSetCustomerActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomer'),
             customer: customerResourceIdentifierType().optional()
          })
}

export function reviewSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function reviewSetLocaleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLocale'),
             locale: Joi.string().optional()
          })
}

export function reviewSetRatingActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setRating'),
             rating: Joi.number().optional()
          })
}

export function reviewSetTargetActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             target: Joi.any().required(),
             action: Joi.string().required().only('setTarget')
          })
}

export function reviewSetTextActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setText'),
             text: Joi.string().optional()
          })
}

export function reviewSetTitleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setTitle'),
             title: Joi.string().optional()
          })
}

export function reviewTransitionStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateResourceIdentifierType().required(),
             action: Joi.string().required().only('transitionState'),
             force: Joi.boolean().optional()
          })
}